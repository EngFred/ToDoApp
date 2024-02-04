package com.engineerfred.kotlin.todoapp.feature_todo.presentation.screens.search_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.engineerfred.kotlin.todoapp.R
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.screens.search_screen.components.SearchBar
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.screens.todo_list_screen.components.TasksList
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.theme.ToDoAppTheme
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.view_models.search_tasks_view_model.SearchTasksScreenEvents
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.view_models.search_tasks_view_model.SearchTasksViewModel
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.view_models.todos_list_view_model.TodosListViewModel

@Composable
fun SearchScreen(
    onBackClicked: () -> Unit,
    onUpdateTask: ( todoId:Long ) -> Unit
) {

    val searchTasksViewModel = hiltViewModel<SearchTasksViewModel>()
    val todosListViewModel = hiltViewModel<TodosListViewModel>()
    val tasks = searchTasksViewModel.searchResults.collectAsState().value

    val bgColor =  if ( todosListViewModel.uiState.isLightTheme )  {
        Color(0xFF0061A4)
    } else {
        Color(0xFF1A1C1E)
    }
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT
    val screenBackgroundImage = if ( isPortrait ) painterResource(id = R.drawable.background_portrait) else painterResource(id = R.drawable.background_landscape)

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = screenBackgroundImage,
            contentDescription = stringResource(id = R.string.screen_background_image),
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxSize(),
            alignment = Alignment.TopStart
        )
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            SearchBar(
                value = searchTasksViewModel.uiState.searchQuery,
                backgroundColor = bgColor,
                onValueChange = {
                    searchTasksViewModel.onEvent( SearchTasksScreenEvents.OnQueryChanged(it) )
                },
                onClearClicked = {
                    searchTasksViewModel.onEvent( SearchTasksScreenEvents.ClearClicked )
                },
                onBackClicked = { onBackClicked.invoke()},
                showCloseIcon = searchTasksViewModel.uiState.searchQuery.isNotEmpty()
            )
            Spacer(modifier = Modifier.height(5.dp))
            TasksList(
                todos = tasks,
                todosListViewModel = todosListViewModel,
                onPrioritize = {
                    searchTasksViewModel.onEvent( SearchTasksScreenEvents.TaskPrioritized(it))
                },
                onCompleted = {
                    searchTasksViewModel.onEvent( SearchTasksScreenEvents.TaskCompletedClicked(it))
                },
                onDeleted = {
                    searchTasksViewModel.onEvent( SearchTasksScreenEvents.DeleteTodoClicked(it))
                },
                onCardClicked = {
                    onUpdateTask.invoke(it)
                },
                isDarkTheme = todosListViewModel.uiState.isLightTheme
            )

        }
    }


}


@Preview
@Composable
fun SearchScreenPreview() {
    ToDoAppTheme {
        SearchScreen({}, {})
    }
}