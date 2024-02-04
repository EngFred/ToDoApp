package com.engineerfred.kotlin.todoapp.feature_todo.data.local.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.engineerfred.kotlin.todoapp.feature_todo.data.local.dao.DeletedTodoDao
import com.engineerfred.kotlin.todoapp.feature_todo.data.local.dao.SavedToPostTodoDao
import com.engineerfred.kotlin.todoapp.feature_todo.data.local.dao.TodoDao
import com.engineerfred.kotlin.todoapp.feature_todo.data.local.entities.DeletedTodoEntity
import com.engineerfred.kotlin.todoapp.feature_todo.data.local.entities.SavedToPostTodoEntity
import com.engineerfred.kotlin.todoapp.feature_todo.data.local.entities.TodoEntity

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