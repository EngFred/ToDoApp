package com.engineerfred.kotlin.todoapp.feature_todo.domain.use_cases

import com.engineerfred.kotlin.todoapp.feature_todo.domain.repository.TasksRepository
import javax.inject.Inject

class GetTodoByIdUseCase @Inject constructor(
    private val tasksRepository: TasksRepository
) {
    operator fun invoke( todoId: Long ) = tasksRepository.getTodoById(todoId)
}