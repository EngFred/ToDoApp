package com.engineerfred.kotlin.todoapp.feature_todo.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface TodoService {

    @GET("todos.json")
    suspend fun getAllRemoteTodos() : List<TodoDto>

//    @GET("todos.json?orderBy=\"id\"")
//    suspend fun getTodoById( @Query("equalTo") id: Long ) : Map<Long, TodoDto>

    @PUT
    suspend fun addTodo( @Url url: String, @Body todo: TodoDto )

    @PUT("todos/{id}.json")
    suspend fun updateTodo( @Path("id") id: Long, @Body todo: TodoDto )

    @DELETE("todos/{id}.json")
    suspend fun deleteTodo( @Path("id") id: Long )

}