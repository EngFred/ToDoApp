package com.engineerfred.kotlin.todoapp.feature_todo.presentation.view_models.save_update_todo_view_model

import com.engineerfred.kotlin.todoapp.feature_todo.domain.models.Todo

data class SaveUpdateTodoScreenState(
    val headerTitle: String = "Update Task",
    val todoId: Long = 1,
    val todoTitle: String = "",
    val description: String = "",
    val timeStamp: Long = 0L,
    val prioritized: Boolean = false,
    val completed: Boolean = false,
    val isSynced: Boolean = false,
    val dueDate: Long? = null,

    val titleErrorMessage: String = "",
    val descriptionErrorMessage: String = "",
    val invalidDateMessage: String = "",
) {
    //for create a todo based on the state values
    val todo = Todo(
        id = todoId,
        title = todoTitle,
        description = description,
        timeStamp = timeStamp,
        completed = completed,
        prioritized = prioritized,
        isSynced = isSynced,
        dueDate = dueDate
    )
}
