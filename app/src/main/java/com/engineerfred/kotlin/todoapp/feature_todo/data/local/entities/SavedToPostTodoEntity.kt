package com.engineerfred.kotlin.todoapp.feature_todo.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("saved_to_post_todos")
data class SavedToPostTodoEntity(
    @PrimaryKey
    val id: Long,
    val title: String,
    val description: String,
    val timeStamp: Long,
    val completed: Boolean,
    val prioritized: Boolean,
    val isSynced: Boolean,
    val dueDate: Long?
)
