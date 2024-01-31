package com.engineerfred.kotlin.todoapp.feature_todo.data.repository

import android.util.Log
import com.engineerfred.kotlin.todoapp.core.util.Resource
import com.engineerfred.kotlin.todoapp.feature_todo.data.di.IoDispatcher
import com.engineerfred.kotlin.todoapp.feature_todo.data.local.SavedToPostTodoEntity
import com.engineerfred.kotlin.todoapp.feature_todo.data.local.TodoDatabase
import com.engineerfred.kotlin.todoapp.feature_todo.data.local.TodoEntity
import com.engineerfred.kotlin.todoapp.feature_todo.data.mappers.toDeletedTodoEntity
import com.engineerfred.kotlin.todoapp.feature_todo.data.mappers.toSavedToPostTodoEntity
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
import retrofit2.HttpException
import java.net.ConnectException
import java.net.UnknownHostException
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

    override fun addTodo( todoEntity: TodoEntity ): Flow<Resource<Unit?>> {
        var newTaskId: Long = 0L
        return flow {
            emit(Resource.Loading)
            Log.i(TAG, "Adding a new todo!")
            newTaskId = cache.todoDao.addUpdateTodo(todoEntity) //not yet synced!
            emit(Resource.Success(null))
            Log.i(TAG, "The new task id is $newTaskId!")
            withContext(NonCancellable) {
                Log.i(TAG, "Todo is added to cache! Syncing in progress...")
                val url = "todos/${newTaskId}.json"
                service.addTodo(url, todoEntity.toTodoDtO().copy( isSynced = true ))
                Log.i(TAG, "The new todo was synced successfully!")
                Log.i(TAG, "Updating the todo in cache!")
                cache.todoDao.addUpdateTodo( todoEntity.copy( isSynced = true ) )
                Log.v(TAG, "Your Todo was cached & synced successfully!")
            }
            emit(Resource.Undefined)
        }.flowOn(ioDispatcher).catch {
            if ( it is CancellationException ) throw it
            if ( it is ConnectException || it is HttpException || it is UnknownHostException ) {
                withContext(NonCancellable) {
                    Log.i(TAG, "Todo is saved in cache but not in the service! it's currently Stored in the 'saved to post' table for later creation in the service as well.")
                    val saveToPostTask = SavedToPostTodoEntity(
                        id = newTaskId,
                        title = todoEntity.title,
                        description = todoEntity.description,
                        timeStamp = todoEntity.timeStamp,
                        completed = todoEntity.completed,
                        archived = todoEntity.completed,
                        isSynced = false
                    )
                    Log.i("SaveToPost", "The new task id inside the catch block for save to Post is $newTaskId")
                    cache.savedToPostTodoDao.addTodo( saveToPostTask )
                    Log.v(TAG, "Todo has been saved in the 'saved to post' table successfully for saving in the service!")
                }
            }
            Log.i(TAG, "Adding todo: $it")
            emit(Resource.Failure("Failed to add todo!"))
        }
    }

    override fun updateTodo( todo: Todo ) : Flow<Resource<Unit?>> {
        return flow{
            emit(Resource.Loading)
            cache.todoDao.addUpdateTodo( todo.toTodoEntity().copy( isSynced = false ) )
            Log.i(TAG, "Todo updated in cache! Syncing in progress...")
            emit(Resource.Success(null))
            withContext(NonCancellable) {
                service.updateTodo( todo.id, todo.toTodoDto().copy( isSynced = true ) )
                Log.i(TAG, "Todo has been synced successfully! Updating cache..." )
                cache.todoDao.addUpdateTodo( todo.toTodoEntity().copy( isSynced = true ) )
                Log.v(TAG, "Todo has been updated successfully!" )
            }
            emit(Resource.Undefined)
        }.flowOn(ioDispatcher).catch{
            if ( it is CancellationException ) throw it
            Log.i(TAG, "Updating todo: $it")
            emit(Resource.Failure("$it"))
        }
    }

    override fun deleteTodo( todo: Todo ) : Flow<Resource<Unit>> {
        return flow {
            emit(Resource.Loading)
            cache.savedToPostTodoDao.deleteTodo(todo.toSavedToPostTodoEntity())
            val todoResult = cache.todoDao.deleteTodo(todo.toTodoEntity())
            emit(Resource.Success(todoResult))
            Log.i(TAG, "Deleting todo from service...")
            withContext(NonCancellable) {
                service.deleteTodo( todo.id )
                Log.v(TAG, "Todo has been deleted successfully from service too!")
            }
            emit(Resource.Undefined)
        }.flowOn(ioDispatcher).catch{
            if ( it is CancellationException ) throw  it
            if ( it is ConnectException || it is HttpException || it is UnknownHostException ) {
               withContext(NonCancellable) {
                   Log.i(TAG, "Todo is deleted from cache but not from service! it's currently Stored in the trash for later deletion from the service as well.")
                   cache.deletedTodoDao.addDeletedTodo(todo.toDeletedTodoEntity())
                   Log.v(TAG, "Todo has been stored in the trash successfully for later deletion from the service!")
               }
            }
            Log.i(TAG, "Deleting todo: $it")
            emit(Resource.Failure("Failed to delete todo!"))
        }
    }

    override fun getSavedToPostTodos(): Flow<Resource<List<Todo>>> {
        return flow {
            emit(Resource.Loading)
            cache.savedToPostTodoDao.getAllTodos().collect{
                if ( it.isEmpty() ) {
                    Log.d("SavedToPost", "No task was found in the database table 'Saved to Post' ")
                    emit(Resource.Undefined)
                } else {
                    emit(Resource.Success( it.map { it.toTodo() } ))
                    Log.d("SavedToPost", "${it.size} tasks were found in database table 'Saved to Post' ")
                }
            }
        }.flowOn(ioDispatcher).catch {
            Log.i(TAG, "Getting Saved to Post todo tasks: $it")
            emit(Resource.Failure("$it"))
        }
    }
}