package com.engineerfred.kotlin.todoapp.feature_todo.data.mappers

import com.engineerfred.kotlin.todoapp.feature_todo.data.local.TodoEntity
import com.engineerfred.kotlin.todoapp.feature_todo.data.remote.TodoDto
import com.engineerfred.kotlin.todoapp.feature_todo.domain.models.Todo

fun Todo.toTodoDto() : TodoDto {
    return TodoDto(
        id = id,
        title = title,
        description = description,
        timeStamp = timeStamp,
        completed = completed,
        archived = archived
    )
}

fun Todo.toTodoEntity() : TodoEntity {
    return TodoEntity(
        id = id,
        title = title,
        description = description,
        timeStamp = timeStamp,
        completed = completed,
        archived = archived
    )
}


fun TodoEntity.toTodo() : Todo{
    return Todo(
        id = id,
        title = title,
        description = description,
        timeStamp = timeStamp,
        completed = completed,
        archived = archived
    )
}

fun TodoDto.toTodo() : Todo{
    return Todo(
        id = id,
        title = title,
        description = description,
        timeStamp = timeStamp,
        completed = completed,
        archived = archived
    )
}


fun TodoEntity.toTodoDtO() : TodoDto{
    return TodoDto(
        id = id,
        title = title,
        description = description,
        timeStamp = timeStamp,
        completed = completed,
        archived = archived
    )
}

fun TodoDto.toTodoEntity() : TodoEntity{
    return TodoEntity(
        id = id,
        title = title,
        description = description,
        timeStamp = timeStamp,
        completed = completed,
        archived = archived
    )
}

