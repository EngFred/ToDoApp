package com.engineerfred.kotlin.todoapp.feature_todo.presentation.view_models.search_tasks_view_model

import com.engineerfred.kotlin.todoapp.feature_todo.domain.models.Todo

sealed class SearchTasksScreenEvents {
    data object ClearClicked : SearchTasksScreenEvents()
    data class OnQueryChanged( val query: String ): SearchTasksScreenEvents()
    data class DeleteTodoClicked( val todo: Todo) : SearchTasksScreenEvents()
    data class TaskCompletedClicked( val todo: Todo) : SearchTasksScreenEvents()
    data class TaskPrioritized(val todo: Todo) : SearchTasksScreenEvents()
}