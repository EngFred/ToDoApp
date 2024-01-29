package com.engineerfred.kotlin.todoapp.feature_todo.domain.use_cases

import com.engineerfred.kotlin.todoapp.feature_todo.domain.models.Todo
import com.engineerfred.kotlin.todoapp.feature_todo.domain.repository.TodosRepository
import javax.inject.Inject

class AddTodoUseCase @Inject constructor(
    private val todosRepository: TodosRepository
) {
    suspend fun invoke( todo: Todo ) = todosRepository.addTodo(todo)
}