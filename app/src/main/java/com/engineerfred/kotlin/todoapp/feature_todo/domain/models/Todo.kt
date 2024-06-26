package com.engineerfred.kotlin.todoapp.feature_todo.domain.models

data class Todo(
    val id: Long,
    val title: String,
    val description: String,
    val timeStamp: Long,
    val completed: Boolean,
    val prioritized: Boolean,
    val isSynced: Boolean = false,
    val dueDate: Long?
)
