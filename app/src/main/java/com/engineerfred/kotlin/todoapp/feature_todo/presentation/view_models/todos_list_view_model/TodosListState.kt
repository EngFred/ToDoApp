package com.engineerfred.kotlin.todoapp.feature_todo.presentation.view_models.todos_list_view_model

data class TodosListState(
    val todosOrder: TodosOrder = TodosOrder.Title( TodosSortingDirection.AtoZ, false )
)