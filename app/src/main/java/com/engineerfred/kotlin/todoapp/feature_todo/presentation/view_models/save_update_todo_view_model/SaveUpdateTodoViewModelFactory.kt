package com.engineerfred.kotlin.todoapp.feature_todo.presentation.view_models.save_update_todo_view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class SaveUpdateTodoViewModelFactory(
    private val saveUpdateTodoViewModelAssistedFactory: SaveUpdateTodoViewModelAssistedFactory,
    private val todoId: Long
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return saveUpdateTodoViewModelAssistedFactory.getTodoById(todoId) as T
    }
}