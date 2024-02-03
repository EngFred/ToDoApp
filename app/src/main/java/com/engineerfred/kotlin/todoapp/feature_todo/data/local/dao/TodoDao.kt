package com.engineerfred.kotlin.todoapp.feature_todo.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.engineerfred.kotlin.todoapp.feature_todo.data.local.entities.TodoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Query("SELECT * FROM todo")
    fun getAllTodos() : Flow<List<TodoEntity>>

    @Query("SELECT * FROM todo WHERE isSynced = 0")
    suspend fun getUnsyncedTodos() : List<TodoEntity>

    @Query("SELECT * FROM toDo WHERE id = :id")
    fun getTodoById(id: Long ) : Flow<TodoEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllRemoteTodos( todos : List<TodoEntity> )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUpdateTodo(todo: TodoEntity) : Long

    @Delete
    suspend fun deleteTodo( todo: TodoEntity)

}