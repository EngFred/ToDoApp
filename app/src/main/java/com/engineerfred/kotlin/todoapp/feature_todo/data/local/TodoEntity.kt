package com.engineerfred.kotlin.todoapp.feature_todo.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("todo")
data class TodoEntity(
    @PrimaryKey( autoGenerate = false )
    val id: Long,
    val title: String,
    val description: String,
    val timeStamp: Long,
    val completed: Boolean,
    val archived: Boolean,
)
