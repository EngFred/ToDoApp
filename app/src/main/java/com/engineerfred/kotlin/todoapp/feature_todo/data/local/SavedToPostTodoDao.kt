package com.engineerfred.kotlin.todoapp.feature_todo.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedToPostTodoDao {
    @Query("SELECT * FROM saved_to_post_todos")
    fun getAllTodos() : Flow<List<SavedToPostTodoEntity>>

    @Query("SELECT * FROM saved_to_post_todos WHERE id = :id")
    suspend fun getTodoById( id: Long ) : SavedToPostTodoEntity?

    @Upsert
    suspend fun addTodo( todo: SavedToPostTodoEntity )

    @Delete
    suspend fun deleteTodo( todo: SavedToPostTodoEntity )

}