package com.engineerfred.kotlin.todoapp.feature_todo.presentation.view_models.todos_list_view_model

import com.engineerfred.kotlin.todoapp.feature_todo.domain.models.Todo

sealed class TodosListEvents {
    data class DeleteTodoClicked( val todo: Todo ) : TodosListEvents()
    data class TodoCompletedClicked( val todo: Todo ) : TodosListEvents()
    data object TodoCardClicked: TodosListEvents()

}