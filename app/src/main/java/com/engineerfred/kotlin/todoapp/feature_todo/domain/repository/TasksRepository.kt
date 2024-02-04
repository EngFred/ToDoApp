package com.engineerfred.kotlin.todoapp.feature_todo.domain.repository

import com.engineerfred.kotlin.todoapp.core.util.Resource
import com.engineerfred.kotlin.todoapp.feature_todo.data.local.entities.TodoEntity
import com.engineerfred.kotlin.todoapp.feature_todo.domain.models.Todo
import kotlinx.coroutines.flow.Flow

interface TasksRepository {
    fun getAllTodosFromLocalCache() : Flow<Resource<List<Todo>>>
    fun getTodoById(id: Long ) : Flow<Resource<Todo?>>
    fun  addTodo( todoEntity: TodoEntity) : Flow<Resource<Unit?>>
    suspend fun  updateTodo( todo: Todo )
    suspend fun  deleteTodo( todo: Todo )
    fun  searchTasks( query: String ) : Flow<List<Todo>>
}