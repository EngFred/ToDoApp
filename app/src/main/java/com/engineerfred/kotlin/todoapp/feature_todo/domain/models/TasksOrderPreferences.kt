package com.engineerfred.kotlin.todoapp.feature_todo.domain.models


data class TasksOrderPreferences(
    val sortingType: String,
    val sortingDirection: String,
    val showAchieved: Boolean
)