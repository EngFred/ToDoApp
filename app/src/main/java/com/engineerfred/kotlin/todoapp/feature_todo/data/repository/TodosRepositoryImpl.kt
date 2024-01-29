package com.engineerfred.kotlin.todoapp.feature_todo.data.repository

import android.util.Log
import com.engineerfred.kotlin.todoapp.core.util.Resource
import com.engineerfred.kotlin.todoapp.feature_todo.data.di.IoDispatcher
import com.engineerfred.kotlin.todoapp.feature_todo.data.local.TodoDatabase
import com.engineerfred.kotlin.todoapp.feature_todo.data.mappers.toTodo
import com.engineerfred.kotlin.todoapp.feature_todo.data.mappers.toTodoDto
import com.engineerfred.kotlin.todoapp.feature_todo.data.mappers.toTodoEntity
import com.engineerfred.kotlin.todoapp.feature_todo.data.remote.TodoService
import com.engineerfred.kotlin.todoapp.feature_todo.domain.models.Todo
import com.engineerfred.kotlin.todoapp.feature_todo.domain.repository.TodosRepository
import kotlinx.coroutines.CoroutineDispatcher
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

    override fun getAllTodosFromLocalCache(): Flow<Resource<List<Todo>>> {
        return flow {
            emit(Resource.Loading)
            cache.todoDao.getAllTodos().collect{
                val todos = it.map { it.toTodo() }
                if ( todos.isEmpty() ) {
                    Log.i("TAG", "Database is empty! Fetching todos from the service.")
                    getTodosFromService()
                }else {
                    Log.i("TAG", "The local cache already has the todos...")
                    emit(Resource.Success(todos))
                }
            }
        }.flowOn( ioDispatcher ).catch {
            Log.i("TAG", "Flow exception: Getting todos from local cache --> $it")
            emit( Resource.Failure("$it") )
        }
    }

    private suspend fun FlowCollector<Resource<List<Todo>>>.getTodosFromService() {
        try {
            val remoteTodos = service.getAllRemoteTodos().filterNotNull()
            cache.todoDao.addAllRemoteTodos(remoteTodos.map { it.toTodoEntity() })
            cache.todoDao.getAllTodos().collect {
                emit(Resource.Success(it.map { it.toTodo() }))
                Log.i("TAG", "Got todos from service, cached them & retrieved them.")
            }
        } catch (ex: Exception) {
            Log.i("TAG", "Failed to get todos from the service + there's now any todos data from local cache")
            emit( Resource.Failure("$ex") )
        }
    }

    //this is the only function i want to operate locally!
    override fun getTodoById(id: Long): Flow<Resource<Todo?>> {
        return flow {
            emit( Resource.Loading )
            cache.todoDao.getTodoIById(id).collect{
                emit( Resource.Success(it?.toTodo()) )
            }
        }.flowOn( ioDispatcher ).catch {
            Log.i("TAG", "Getting todo by id from local cache: $it")
            emit( Resource.Failure("$it") )
        }
    }

    override suspend fun addTodo(todo: Todo) : Resource<Unit> {
        return try {
            Resource.Loading
            Log.i("TAG", "The new todo id is ${todo.id}")
            val url = "todos/${todo.id}.json"
            val serverResponse = service.addTodo( url, todo.toTodoDto() )
            if ( serverResponse.isSuccessful ) {
                try {
                    val cacheResponse = cache.todoDao.addUpdateTodo(todo.toTodoEntity())
                    Log.i("TAG", "Todo added successfully!")
                    Resource.Success( cacheResponse )
                } catch (ex: Exception ) {
                    Log.i("TAG", "Adding Local todo: $ex")
                    Resource.Failure("$ex")
                }
            } else {
                Log.i("TAG", "Adding Remote todo: ${serverResponse.message()}")
                Resource.Failure("Response was not successful!")
            }
        } catch ( ex: Exception ) {
            Log.i("TAG", "Adding Remote todo: $ex")
            Resource.Failure("Failed to add todo!")
        }
    }

    override suspend fun updateTodo( todo: Todo ) : Resource<Unit> {
        return try {
            Resource.Loading
            val serverResponse  = service.updateTodo( todo.id, todo.toTodoDto())
            if ( serverResponse.isSuccessful ) {
                try {
                    val cacheResponse =  cache.todoDao.addUpdateTodo(todo.toTodoEntity()) //updated the cache
                    Log.i("TAG", "Todo updated successfully!")
                    Resource.Success( cacheResponse )
                }catch ( ex: Exception ) {
                    Log.i("TAG", "Updating Local todo: $ex")
                    Resource.Failure("$ex")
                }
            } else {
                Log.i("TAG", "Server response failed: ${serverResponse.message()}")
                Resource.Failure("Failed to update todo!")
            }
        } catch ( ex: Exception ) {
            Log.i("TAG", "Updating todo: $ex")
            Resource.Failure("Failed to update todo!")
        }
    }

    override suspend fun deleteTodo(todo: Todo) : Resource<Unit> {
        return try {
            Resource.Loading
            val serverResponse = service.deleteTodo( todo.id )
            if ( serverResponse.isSuccessful ) {
                try {
                    val cacheResponse = cache.todoDao.deleteTodo(todo.toTodoEntity())
                    Log.i("TAG", "Todo deleted successfully!")
                    Resource.Success(cacheResponse)
                } catch ( ex: Exception ) {
                    Log.i("TAG", "Deleting Local todo: $ex")
                    Resource.Failure("$ex")
                }
            } else {
                Log.i("TAG", "Server response failed: ${serverResponse.message()}")
                Resource.Failure("Failed to delete todo!")
            }
        }catch ( ex: Exception ) {
            Log.i("TAG", "Deleting todo: $ex")
            Resource.Failure("Failed to delete todo!")
        }
    }
}