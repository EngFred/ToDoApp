package com.engineerfred.kotlin.todoapp.feature_todo.presentation.screens.todo_list_screen.components

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.engineerfred.kotlin.todoapp.feature_todo.domain.models.Todo
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.util.setTodosSortOrder
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.util.showDeleteAlertDialog
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.view_models.todos_list_view_model.TodosListViewModel

@Composable
fun TodosList(
    modifier: Modifier = Modifier,
    todos: List<Todo>,
    todosListViewModel: TodosListViewModel,
    onAchieved: (Todo) -> Unit,
    onCompleted: (Todo) -> Unit,
    onDeleted: (Todo) -> Unit,
    onCardClicked: (todoId: Long) -> Unit,
    isDarkTheme: Boolean
) {

    val filterTodos = setTodosSortOrder(todosListViewModel, todos)
    val context = LocalContext.current
    
    //attend a live performance and buy groceries and cook a new recipe

    LaunchedEffect(Unit) {
        Log.v("TodoApplication", "Unsynced todos are ${todos.filter { !it.isSynced }.size}")
    }

    LazyColumn( modifier ) {
        items(
            count = filterTodos.size,
            key = { filterTodos[it].id },
        ) {
            val todo = filterTodos[it]
            TodoCard(
                todo = todo,
                onDeleteClicked = {
                     showDeleteAlertDialog(
                         task = todo.title,
                         context = context,
                         onDeleteClicked = { onDeleted.invoke(todo) }
                     )
                },
                onAchieveClicked = { onAchieved.invoke(todo) },
                onCardClicked = { onCardClicked.invoke(todo.id) },
                onCompletedClicked = { onCompleted.invoke(todo) },
                isDarkTheme = isDarkTheme
            )
        }
    }

}