package com.engineerfred.kotlin.todoapp.feature_todo.presentation.view_models.save_update_todo_view_model

sealed class SaveUpdateTodoScreenEvents {
    data class TitleChanged( val title: String ) : SaveUpdateTodoScreenEvents()
    data class DescriptionChanged( val description: String ) : SaveUpdateTodoScreenEvents()
    data object AchieveClicked: SaveUpdateTodoScreenEvents()
    data class DueDateSelected( val date: Long ): SaveUpdateTodoScreenEvents()
    data object CompletedClicked: SaveUpdateTodoScreenEvents()
    data object DeleteClicked: SaveUpdateTodoScreenEvents()
    data object SaveClicked: SaveUpdateTodoScreenEvents()

}