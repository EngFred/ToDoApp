package com.engineerfred.kotlin.todoapp.feature_todo.domain.repository

import com.engineerfred.kotlin.todoapp.feature_todo.domain.models.TasksOrderPreferences
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    suspend fun saveTheme( isLightTheme: Boolean )
    fun getTheme() : Flow<Boolean>

    suspend fun saveTasksOrdering(tasksOrderPreferences: TasksOrderPreferences)

    fun getTasksOrdering() : Flow<TasksOrderPreferences>

}