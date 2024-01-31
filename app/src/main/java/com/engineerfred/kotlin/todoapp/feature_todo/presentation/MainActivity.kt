package com.engineerfred.kotlin.todoapp.feature_todo.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.navigation.AppNavigationGraph
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.theme.ToDoAppTheme
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.view_models.save_update_todo_view_model.SaveUpdateTodoViewModelAssistedFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var saveUpdateTodoViewModelAssistedFactory: SaveUpdateTodoViewModelAssistedFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            ToDoAppTheme {
                TodoApplication(saveUpdateTodoViewModelAssistedFactory)
            }
        }
    }
}

@Composable
private fun TodoApplication(
    saveUpdateTodoViewModelAssistedFactory: SaveUpdateTodoViewModelAssistedFactory
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        AppNavigationGraph(saveUpdateTodoViewModelAssistedFactory = saveUpdateTodoViewModelAssistedFactory)
    }
}