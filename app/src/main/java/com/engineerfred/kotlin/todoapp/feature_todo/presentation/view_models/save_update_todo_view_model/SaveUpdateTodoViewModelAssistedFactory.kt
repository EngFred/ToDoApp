package com.engineerfred.kotlin.todoapp.feature_todo.presentation.view_models.save_update_todo_view_model

import dagger.assisted.AssistedFactory

@AssistedFactory
interface SaveUpdateTodoViewModelAssistedFactory {
    fun getTodoById( todoId: Long ) : SaveUpdateTodoViewModel
}