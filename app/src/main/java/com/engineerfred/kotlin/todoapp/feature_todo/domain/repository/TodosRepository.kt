package com.engineerfred.kotlin.todoapp.feature_todo.domain.repository

import com.engineerfred.kotlin.todoapp.core.util.Resource
import com.engineerfred.kotlin.todoapp.feature_todo.domain.models.Todo
import kotlinx.coroutines.flow.Flow

interface TodosRepository {
    fun getAllTodosFromLocalCache() : Flow<Resource<List<Todo>>>
    fun getTodoById(id: Long ) : Flow<Resource<Todo?>>
    suspend fun  addTodo( todo: Todo ) : Resource<Unit>
    suspend fun  updateTodo( todo: Todo ) : Resource<Unit>
    suspend fun  deleteTodo( todo: Todo ) : Resource<Unit>
}