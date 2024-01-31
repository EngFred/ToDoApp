package com.engineerfred.kotlin.todoapp.feature_todo.data.mappers

import com.engineerfred.kotlin.todoapp.feature_todo.data.local.DeletedTodoEntity
import com.engineerfred.kotlin.todoapp.feature_todo.data.local.SavedToPostTodoEntity
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
        archived = archived,
        isSynced = isSynced
    )
}

fun Todo.toTodoEntity() : TodoEntity {
    return TodoEntity(
        id = id,
        title = title,
        description = description,
        timeStamp = timeStamp,
        completed = completed,
        archived = archived,
        isSynced = isSynced
    )
}

fun Todo.toDeletedTodoEntity() : DeletedTodoEntity {
    return DeletedTodoEntity(
        id = id,
        title = title,
        description = description,
        timeStamp = timeStamp,
        completed = completed,
        archived = archived,
        isSynced = isSynced
    )
}

fun TodoEntity.toTodo() : Todo{
    return Todo(
        id = id,
        title = title,
        description = description,
        timeStamp = timeStamp,
        completed = completed,
        archived = archived,
        isSynced = isSynced
    )
}

fun TodoEntity.toSaveToPostTodoEntity() : SavedToPostTodoEntity{
    return SavedToPostTodoEntity(
        id = id,
        title = title,
        description = description,
        timeStamp = timeStamp,
        completed = completed,
        archived = archived,
        isSynced = isSynced
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
        archived = archived,
        isSynced = isSynced
    )
}

fun TodoDto.toTodoEntity() : TodoEntity{
    return TodoEntity(
        id = id,
        title = title,
        description = description,
        timeStamp = timeStamp,
        completed = completed,
        archived = archived,
        isSynced = isSynced
    )
}

fun SavedToPostTodoEntity.toTodo() : Todo{
    return Todo(
        id = id,
        title = title,
        description = description,
        timeStamp = timeStamp,
        completed = completed,
        archived = archived,
        isSynced = isSynced
    )
}

fun Todo.toSavedToPostTodoEntity() : SavedToPostTodoEntity{
    return SavedToPostTodoEntity(
        id = id,
        title = title,
        description = description,
        timeStamp = timeStamp,
        completed = completed,
        archived = archived,
        isSynced = isSynced
    )
}

