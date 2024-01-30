package com.engineerfred.kotlin.todoapp.feature_todo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database( entities = [ TodoEntity::class ], version = 1, exportSchema = false )
abstract class TodoDatabase : RoomDatabase() {
    abstract val todoDao: TodoDao

    companion object {
        const val DATABASE_NAME = "todo.db"
    }
}