package com.engineerfred.kotlin.todoapp.feature_todo.data.mappers

import com.engineerfred.kotlin.todoapp.feature_todo.data.local.entities.DeletedTodoEntity
import com.engineerfred.kotlin.todoapp.feature_todo.data.local.entities.SavedToPostTodoEntity
import com.engineerfred.kotlin.todoapp.feature_todo.data.local.entities.TodoEntity
import com.engineerfred.kotlin.todoapp.feature_todo.data.remote.TodoDto
import com.engineerfred.kotlin.todoapp.feature_todo.domain.models.Todo

fun Todo.toTodoDto() : TodoDto {
    return TodoDto(
        id = id,
        title = title,
        description = description,
        timeStamp = timeStamp,
        completed = completed,
        prioritized = prioritized,
        isSynced = isSynced,
        dueDate = dueDate
    )
}

fun Todo.toTodoEntity() : TodoEntity {
    return TodoEntity(
        id = id,
        title = title,
        description = description,
        timeStamp = timeStamp,
        completed = completed,
        prioritized = prioritized,
        isSynced = isSynced,
        dueDate = dueDate
    )
}

fun Todo.toDeletedTodoEntity() : DeletedTodoEntity {
    return DeletedTodoEntity(
        id = id,
        title = title,
        description = description,
        timeStamp = timeStamp,
        completed = completed,
        prioritized = prioritized,
        isSynced = isSynced,
        dueDate = dueDate
    )
}

fun TodoEntity.toTodo() : Todo{
    return Todo(
        id = id,
        title = title,
        description = description,
        timeStamp = timeStamp,
        completed = completed,
        prioritized = prioritized,
        isSynced = isSynced,
        dueDate = dueDate
    )
}

fun TodoEntity.toSaveToPostTodoEntity() : SavedToPostTodoEntity {
    return SavedToPostTodoEntity(
        id = id,
        title = title,
        description = description,
        timeStamp = timeStamp,
        completed = completed,
        prioritized = prioritized,
        isSynced = isSynced,
        dueDate = dueDate
    )
}

fun TodoDto.toTodo() : Todo{
    return Todo(
        id = id,
        title = title,
        description = description,
        timeStamp = timeStamp,
        completed = completed,
        prioritized = prioritized,
        dueDate = dueDate
    )
}


fun TodoEntity.toTodoDtO() : TodoDto{
    return TodoDto(
        id = id,
        title = title,
        description = description,
        timeStamp = timeStamp,
        completed = completed,
        prioritized = prioritized,
        isSynced = isSynced,
        dueDate = dueDate
    )
}

fun TodoDto.toTodoEntity() : TodoEntity {
    return TodoEntity(
        id = id,
        title = title,
        description = description,
        timeStamp = timeStamp,
        completed = completed,
        prioritized = prioritized,
        isSynced = isSynced,
        dueDate = dueDate
    )
}

fun SavedToPostTodoEntity.toTodo() : Todo{
    return Todo(
        id = id,
        title = title,
        description = description,
        timeStamp = timeStamp,
        completed = completed,
        prioritized = prioritized,
        isSynced = isSynced,
        dueDate = dueDate
    )
}

fun Todo.toSavedToPostTodoEntity() : SavedToPostTodoEntity {
    return SavedToPostTodoEntity(
        id = id,
        title = title,
        description = description,
        timeStamp = timeStamp,
        completed = completed,
        prioritized = prioritized,
        isSynced = isSynced,
        dueDate = dueDate
    )
}

fun DeletedTodoEntity.toTodo() : Todo {
    return Todo(
        id = id,
        title = title,
        description = description,
        timeStamp = timeStamp,
        completed = completed,
        prioritized = prioritized,
        isSynced = isSynced,
        dueDate = dueDate
    )
}

fun SavedToPostTodoEntity.toTodoDto() : TodoDto{
    return TodoDto(
        id = id,
        title = title,
        description = description,
        timeStamp = timeStamp,
        completed = completed,
        prioritized = prioritized,
        isSynced = isSynced,
        dueDate = dueDate
    )
}

fun TodoEntity.toTodoDto() : TodoDto{
    return TodoDto(
        id = id,
        title = title,
        description = description,
        timeStamp = timeStamp,
        completed = completed,
        prioritized = prioritized,
        isSynced = isSynced,
        dueDate = dueDate
    )
}

