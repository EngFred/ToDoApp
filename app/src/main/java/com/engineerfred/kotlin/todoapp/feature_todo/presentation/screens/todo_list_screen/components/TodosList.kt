package com.engineerfred.kotlin.todoapp.feature_todo.presentation.screens.todo_list_screen.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.engineerfred.kotlin.todoapp.feature_todo.domain.models.Todo
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.util.setTodosSortOrder
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.view_models.todos_list_view_model.TodosListViewModel

@Composable
fun TodosList(
    modifier: Modifier = Modifier,
    todos: List<Todo>,
    todosListViewModel: TodosListViewModel
) {

    val filterTodos = setTodosSortOrder(todosListViewModel, todos)

    LazyColumn( modifier ) {
        items( items = filterTodos ) {
            TodoCard(
                todo = it,
                onDeleteClicked = { /*TODO*/ },
                onAchieveClicked = { /*TODO*/ },
                onCardClicked = { /*TODO*/ }) {
            }
        }
    }

}