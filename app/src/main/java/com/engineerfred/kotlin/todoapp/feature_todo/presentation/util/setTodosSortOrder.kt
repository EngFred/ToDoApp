package com.engineerfred.kotlin.todoapp.feature_todo.presentation.util

import com.engineerfred.kotlin.todoapp.feature_todo.domain.models.Todo
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.view_models.todos_list_view_model.TodosListViewModel
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.view_models.todos_list_view_model.TodosOrder
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.view_models.todos_list_view_model.TodosSortingDirection

fun setTodosSortOrder(
    todosListViewModel: TodosListViewModel,
    todos: List<Todo>
): List<Todo> {
    return when (todosListViewModel.uiState.todosOrder.sortingDirection) {
        TodosSortingDirection.ZtoA -> {
            when (todosListViewModel.uiState.todosOrder) {
                is TodosOrder.Title -> {
                    when {
                        todosListViewModel.uiState.todosOrder.showAchieved -> {
                            todos.sortedByDescending { it.title.lowercase() }.filter { it.archived }
                        }

                        else -> todos.sortedByDescending { it.title.lowercase() }
                    }
                }

                is TodosOrder.Time -> {
                    when {
                        todosListViewModel.uiState.todosOrder.showAchieved -> {
                            todos.sortedByDescending { it.timeStamp }.filter { it.archived }
                        }

                        else -> todos.sortedByDescending { it.timeStamp }
                    }
                }

                is TodosOrder.Completed -> {
                    when {
                        todosListViewModel.uiState.todosOrder.showAchieved -> {
                            todos.sortedByDescending { it.completed }.filter { it.archived }
                        }

                        else -> todos.sortedByDescending { it.completed }
                    }
                }
            }
        }

        TodosSortingDirection.AtoZ -> {
            when (todosListViewModel.uiState.todosOrder) {
                is TodosOrder.Title -> {
                    when {
                        todosListViewModel.uiState.todosOrder.showAchieved -> {
                            todos.sortedBy { it.title.lowercase() }.filter { it.archived }
                        }

                        else -> todos.sortedBy { it.title.lowercase() }
                    }
                }

                is TodosOrder.Time -> {
                    when {
                        todosListViewModel.uiState.todosOrder.showAchieved -> {
                            todos.sortedBy { it.timeStamp }.filter { it.archived }
                        }

                        else -> todos.sortedBy { it.timeStamp }
                    }
                }

                is TodosOrder.Completed -> {
                    when {
                        todosListViewModel.uiState.todosOrder.showAchieved -> {
                            todos.sortedBy { it.completed }.filter { it.archived }
                        }

                        else -> todos.sortedBy { it.completed }
                    }
                }
            }
        }
    }
}