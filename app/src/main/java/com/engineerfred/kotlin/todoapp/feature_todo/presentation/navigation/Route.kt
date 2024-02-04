package com.engineerfred.kotlin.todoapp.feature_todo.presentation.navigation

sealed class Route( val destination: String ) {
    data object TodosListScreen: Route("todos_list_screen")
    data object TodosSaveUpdateScreen: Route("todos_save_update_screen")
    data object SearchScreen: Route("search_screen")
}