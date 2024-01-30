package com.engineerfred.kotlin.todoapp.core.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.engineerfred.kotlin.todoapp.feature_todo.domain.models.Todo

data class TodoColors(
    val backgroundColor: Color,
    val textColor: Color,
    val achieveIconColor: Color,
    val completedIconColor: Color,
)

@Composable
fun getTodoColors(todo: Todo) : TodoColors {
    return if ( todo.archived ) {
        TodoColors(
            backgroundColor = MaterialTheme.colorScheme.secondary.copy(alpha = .6f),
            textColor = MaterialTheme.colorScheme.onSecondary,
            achieveIconColor = MaterialTheme.colorScheme.onSecondary,
            completedIconColor = if ( todo.completed ) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.onSecondary
        )
    }else {
        TodoColors(
            backgroundColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = .6f),
            textColor = MaterialTheme.colorScheme.onPrimaryContainer,
            achieveIconColor = MaterialTheme.colorScheme.secondary,
            completedIconColor = if ( todo.completed ) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.secondary
        )
    }
}
