package com.engineerfred.kotlin.todoapp.feature_todo.presentation.view_models.todos_list_view_model

data class TodosListScreenState(
    val todosOrder: TodosOrder = TodosOrder.Time( TodosSortingDirection.AtoZ, false ),
    val isLightTheme: Boolean = true
)