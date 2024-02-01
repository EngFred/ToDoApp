package com.engineerfred.kotlin.todoapp.feature_todo.domain.repository

import com.engineerfred.kotlin.todoapp.core.util.Resource
import com.engineerfred.kotlin.todoapp.feature_todo.data.local.TodoEntity
import com.engineerfred.kotlin.todoapp.feature_todo.domain.models.Todo
import kotlinx.coroutines.flow.Flow

interface TasksRepository {
    fun getAllTodosFromLocalCache() : Flow<Resource<List<Todo>>>
    fun getTodoById(id: Long ) : Flow<Resource<Todo?>>
    fun  addTodo( todoEntity: TodoEntity ) : Flow<Resource<Unit?>>
    fun  updateTodo( todo: Todo ) : Flow<Resource<Unit?>>
    fun  deleteTodo( todo: Todo ) : Flow<Resource<Unit>>
    fun  getAllTodosThatWereCreateWhileOffline(): Flow<Resource<List<Todo>>>
    fun  getAllTodosThatWereDeletedWhileOffline(): Flow<Resource<List<Todo>>>
}