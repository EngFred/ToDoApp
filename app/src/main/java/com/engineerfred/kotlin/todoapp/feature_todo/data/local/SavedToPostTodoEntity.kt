package com.engineerfred.kotlin.todoapp.feature_todo.data.local

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
    val archived: Boolean,
    val isSynced: Boolean
)
