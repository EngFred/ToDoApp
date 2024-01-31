package com.engineerfred.kotlin.todoapp.feature_todo.domain.use_cases

import com.engineerfred.kotlin.todoapp.feature_todo.data.local.TodoEntity
import com.engineerfred.kotlin.todoapp.feature_todo.domain.repository.TodosRepository
import javax.inject.Inject

class AddTodoUseCase @Inject constructor(
    private val todosRepository: TodosRepository
) {
    fun invoke( todoEntity: TodoEntity ) = todosRepository.addTodo(todoEntity)
}