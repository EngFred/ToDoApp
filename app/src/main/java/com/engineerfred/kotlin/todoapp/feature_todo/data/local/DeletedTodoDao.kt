package com.engineerfred.kotlin.todoapp.feature_todo.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface DeletedTodoDao {

    @Query("SELECT * FROM deleted_todos")
    fun getAllDeletedTodos() : Flow<List<DeletedTodoEntity>>

    @Upsert
    suspend fun addDeletedTodo( todo: DeletedTodoEntity )

    @Delete
    suspend fun deleteTodo( deletedTodo: DeletedTodoEntity )

}