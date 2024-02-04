package com.engineerfred.kotlin.todoapp.feature_todo.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.screens.search_screen.SearchScreen
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.screens.todo_list_screen.TodosListScreen
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.screens.todo_new_update_screen.SaveUpdateTodoScreen
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.view_models.save_update_todo_view_model.SaveUpdateTodoViewModelAssistedFactory

@Composable
fun AppNavigationGraph(
    navHostController: NavHostController = rememberNavController(),
    saveUpdateTodoViewModelAssistedFactory: SaveUpdateTodoViewModelAssistedFactory,
) {
    
    NavHost(
        navController = navHostController, 
        startDestination = Route.TodosListScreen.destination
    ) {
        
        composable( route = Route.TodosListScreen.destination) {
            TodosListScreen(
                onCreateTask = {
                    navHostController.navigate( Route.TodosSaveUpdateScreen.destination ) {
                        popUpTo(Route.TodosSaveUpdateScreen.destination)
                        launchSingleTop = true
                    }
                }, onUpdateTask = {
                    navHostController.navigate( "${Route.TodosSaveUpdateScreen.destination }?todoId=$it") {
                        popUpTo(Route.TodosSaveUpdateScreen.destination)
                        launchSingleTop = true
                    }
                }, onSearchClicked = {
                    navHostController.navigate( Route.SearchScreen.destination ) {
                        popUpTo( Route.SearchScreen.destination )
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = "${Route.TodosSaveUpdateScreen.destination}?todoId={todoId}",
            arguments = listOf(
                navArgument("todoId"){
                    type = NavType.LongType
                    defaultValue = -1L
                }
            ),
        ) {
            val todoId = it.arguments?.getLong("todoId")!!
            SaveUpdateTodoScreen(
                saveUpdateTodoViewModelAssistedFactory = saveUpdateTodoViewModelAssistedFactory,
                onBackClicked = { navHostController.navigateUp() },
                todoId = todoId
            )
        }

        composable(
            route = Route.SearchScreen.destination
        ) {
            SearchScreen(
                onBackClicked = {navHostController.navigateUp()},
                onUpdateTask = {
                    navHostController.navigate( "${Route.TodosSaveUpdateScreen.destination }?todoId=$it") {
                        popUpTo(Route.TodosSaveUpdateScreen.destination)
                        launchSingleTop = true
                    }
                }
            )
        }
        
    }
    
}