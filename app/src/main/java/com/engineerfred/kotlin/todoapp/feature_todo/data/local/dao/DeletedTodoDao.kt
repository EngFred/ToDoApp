package com.engineerfred.kotlin.todoapp.feature_todo.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.engineerfred.kotlin.todoapp.feature_todo.data.local.entities.DeletedTodoEntity

@Dao
interface DeletedTodoDao {

    @Query("SELECT * FROM deleted_todos")
    suspend fun getAllDeletedTodos() : List<DeletedTodoEntity>

    @Upsert
    suspend fun addDeletedTodo( todo: DeletedTodoEntity)

    @Delete
    suspend fun deleteTodo( deletedTodo: DeletedTodoEntity)

}