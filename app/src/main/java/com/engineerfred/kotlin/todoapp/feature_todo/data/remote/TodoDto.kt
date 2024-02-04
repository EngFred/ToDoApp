package com.engineerfred.kotlin.todoapp.feature_todo.data.remote

data class TodoDto(
    val id: Long,
    val title: String,
    val description: String,
    val timeStamp: Long,
    val completed: Boolean,
    val prioritized: Boolean,
    val isSynced: Boolean,
    val dueDate: Long?
)
