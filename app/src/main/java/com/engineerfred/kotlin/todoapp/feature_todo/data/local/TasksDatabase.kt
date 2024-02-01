package com.engineerfred.kotlin.todoapp.feature_todo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        TodoEntity::class,
        DeletedTodoEntity::class,
        SavedToPostTodoEntity::class
                ],
    version = 1,
    exportSchema = false
)
abstract class TasksDatabase : RoomDatabase() {
    abstract val todoDao: TodoDao
    abstract val deletedTodoDao: DeletedTodoDao
    abstract val savedToPostTodoDao: SavedToPostTodoDao

    companion object {
        const val DATABASE_NAME = "todo.db"
    }

}