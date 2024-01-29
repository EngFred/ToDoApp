package com.engineerfred.kotlin.todoapp.feature_todo.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Query("SELECT * FROM todo")
    fun getAllTodos() : Flow<List<TodoEntity>>

    @Query("SELECT * FROM toDo WHERE id = :id")
    fun getTodoIById(id: Long ) : Flow<TodoEntity?>

    @Upsert
    suspend fun addAllRemoteTodos( todos : List<TodoEntity> )

    @Upsert
    suspend fun addUpdateTodo(todo: TodoEntity )

    @Delete
    suspend fun deleteTodo( todo: TodoEntity )

}