package com.engineerfred.kotlin.todoapp.feature_todo.data.repository

import android.util.Log
import com.engineerfred.kotlin.todoapp.core.util.Resource
import com.engineerfred.kotlin.todoapp.feature_todo.data.di.IoDispatcher
import com.engineerfred.kotlin.todoapp.feature_todo.data.local.TodoDatabase
import com.engineerfred.kotlin.todoapp.feature_todo.data.mappers.toTodo
import com.engineerfred.kotlin.todoapp.feature_todo.data.mappers.toTodoDtO
import com.engineerfred.kotlin.todoapp.feature_todo.data.mappers.toTodoDto
import com.engineerfred.kotlin.todoapp.feature_todo.data.mappers.toTodoEntity
import com.engineerfred.kotlin.todoapp.feature_todo.data.remote.TodoService
import com.engineerfred.kotlin.todoapp.feature_todo.domain.models.Todo
import com.engineerfred.kotlin.todoapp.feature_todo.domain.repository.TodosRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TodosRepositoryImpl @Inject constructor (
    private val service: TodoService,
    private val cache: TodoDatabase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : TodosRepository {
    
    
    companion object {
        const val TAG = "TodoApplication"
    }

    override fun getAllTodosFromLocalCache(): Flow<Resource<List<Todo>>> {
        return flow {
            emit(Resource.Loading)
            cache.todoDao.getAllTodos().collect{
                val todos = it.map { it.toTodo() }
                if ( todos.isEmpty() ) {
                    Log.i(TAG, "Database is empty! Fetching todos from the service.")
                    getTodosFromService()
                }else {
                    Log.i(TAG, "The local cache already has the todos...")
                    emit(Resource.Success(todos))
                }
            }
            //TODO("Implement a work manager for synchronizing all cached todos from cache if they are not")
        }.flowOn( ioDispatcher ).catch {
            Log.i(TAG, "Flow exception: Getting todos from local cache --> $it")
            emit( Resource.Failure("$it") )
        }
    }

    private suspend fun FlowCollector<Resource<List<Todo>>>.getTodosFromService() {
        try {
            val remoteTodos = service.getAllRemoteTodos().filterNotNull()
            cache.todoDao.addAllRemoteTodos(remoteTodos.map { it.toTodoEntity() })
            cache.todoDao.getAllTodos().collect {
                emit(Resource.Success(it.map { it.toTodo() }))
                Log.i(TAG, "Got todos from service, cached them & retrieved them.")
            }
        } catch (ex: Exception) {
            if ( ex is CancellationException ) throw ex
            Log.i(TAG, "Failed to get todos from the service + there's now any todos data from local cache")
            emit( Resource.Failure("$ex") )
        }
    }

    override fun getTodoById(id: Long): Flow<Resource<Todo?>> {
        return flow {
            emit( Resource.Loading )
            cache.todoDao.getTodoById(id).collect{
                emit( Resource.Success(it?.toTodo()) )
            }
        }.flowOn( ioDispatcher ).catch {
            Log.i(TAG, "Getting todo by id from local cache: $it")
            emit( Resource.Failure("$it") )
        }
    }

    override suspend fun addTodo(todo: Todo) : Resource<Unit> {
        return try {
            Resource.Loading
            Log.i(TAG, "Adding a new todo!")
            Log.i(TAG, "The new todo id is ${todo.id} ***I think by here it is 0L ( the default )..i don't know")
            val addResult = cache.todoDao.addUpdateTodo(todo.toTodoEntity()) //not yet synced!
            Resource.Success(addResult)
            withContext(NonCancellable) {
                Log.i(TAG, "Todo is added to cache! Syncing in progress...")
                cache.todoDao.getTodoById(todo.id).collect{
                    it?.let {
                        val url = "todos/${it.id}.json"
                        service.addTodo(url, it.toTodoDtO().copy( isSynced = true ))
                        Log.i(TAG, "The new todo was synced successfully!")
                        Log.i(TAG, "Updating the todo in cache!")
                        cache.todoDao.addUpdateTodo( it.copy( isSynced = true ) )
                        Log.v(TAG, "Your Todo was cached & synced successfully!")
                    }
                }
            }
            Resource.Undefined
        } catch ( ex: Exception ) {
            if ( ex is CancellationException ) throw ex
            Log.i(TAG, "Adding todo: $ex")
            Resource.Failure("Failed to add todo!")
        }
    }

    override suspend fun updateTodo( todo: Todo ) : Resource<Unit> {
        return try {
            Resource.Loading
            val todoResult = cache.todoDao.addUpdateTodo( todo.toTodoEntity().copy( isSynced = false ) )
            Resource.Success(todoResult)
            withContext(NonCancellable) {
                Log.i(TAG, "Todo updated in cache! Syncing in progress...")
                service.updateTodo( todo.id, todo.toTodoDto().copy( isSynced = true ) )
                Log.i(TAG, "Todo has been synced synced successfully! Updating cache..." )
                cache.todoDao.addUpdateTodo( todo.toTodoEntity().copy( isSynced = true ) )
                Log.v(TAG, "Todo has been updated successfully!" )
            }
            Resource.Undefined
        } catch ( ex: Exception ) {
            if ( ex is CancellationException ) throw ex
            Log.i(TAG, "Updating todo: $ex")
            Resource.Failure("Failed to update todo!")
        }
    }

    override suspend fun deleteTodo( todo: Todo ) : Resource<Unit> {
        return try {
            Resource.Loading
            val todoResult = cache.todoDao.deleteTodo(todo.toTodoEntity())
            Log.i(TAG, "Todo deleted successfully from cache! Now deleting from service...")
            Resource.Success(todoResult)
            withContext(NonCancellable) {
                service.deleteTodo( todo.id )
                Log.v(TAG, "Todo has been deleted successfully!")
            }
            Resource.Undefined
        }catch ( ex: Exception ) {
            if ( ex is CancellationException ) throw  ex
            Log.i(TAG, "Deleting todo: $ex")
            Resource.Failure("Failed to delete todo!")
        }
    }
}