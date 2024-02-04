package com.engineerfred.kotlin.todoapp.core.util

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
    return if ( todo.prioritized ) {
        TodoColors(
            backgroundColor = Color(0xFF006E1C).copy(alpha = .6f),
            textColor = Color(0xFFFFFFFF),
            achieveIconColor = Color(0xFF00390A),
            completedIconColor = if ( todo.completed ) Color(0xFFFFDCBE) else Color(0xFFFFFFFF)
        )
    }else {
        TodoColors(
            backgroundColor = Color(0xFFD1E4FF).copy(alpha = .6f),
            textColor = Color(0xFF00390A),
            achieveIconColor = Color(0xFF006E1C),
            completedIconColor = if ( todo.completed ) Color(0xFF9ECAFF) else Color(0xFF006E1C)
        )
    }
}
