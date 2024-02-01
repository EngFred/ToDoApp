package com.engineerfred.kotlin.todoapp.feature_todo.domain.use_cases

import com.engineerfred.kotlin.todoapp.feature_todo.domain.repository.TasksRepository
import javax.inject.Inject

class GetAllTasksCreatedWhileOfflineUseCase @Inject constructor(
    private val tasksRepository: TasksRepository
) {
    operator fun invoke() = tasksRepository.getAllTodosThatWereCreateWhileOffline()
}