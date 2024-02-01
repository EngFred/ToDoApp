package com.engineerfred.kotlin.todoapp.feature_todo.domain.use_cases

import com.engineerfred.kotlin.todoapp.feature_todo.domain.models.Todo
import com.engineerfred.kotlin.todoapp.feature_todo.domain.repository.TasksRepository
import javax.inject.Inject

class UpdateTodoUseCase @Inject constructor(
    private val tasksRepository: TasksRepository
) {
    fun invoke( todo: Todo ) = tasksRepository.updateTodo(todo)
}