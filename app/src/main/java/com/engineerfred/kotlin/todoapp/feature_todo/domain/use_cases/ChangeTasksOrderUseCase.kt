package com.engineerfred.kotlin.todoapp.feature_todo.domain.use_cases

import com.engineerfred.kotlin.todoapp.feature_todo.domain.models.TasksOrderPreferences
import com.engineerfred.kotlin.todoapp.feature_todo.domain.repository.PreferencesRepository
import javax.inject.Inject

class ChangeTasksOrderUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke( tasksOrderPreferences: TasksOrderPreferences ) = preferencesRepository.saveTasksOrdering(tasksOrderPreferences)
}