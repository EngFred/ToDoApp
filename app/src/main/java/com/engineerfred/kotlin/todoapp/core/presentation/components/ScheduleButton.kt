package com.engineerfred.kotlin.todoapp.core.presentation.components

import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.engineerfred.kotlin.todoapp.R
import com.engineerfred.kotlin.todoapp.feature_todo.domain.models.Todo

@Composable
fun ScheduleButton(
    modifier: Modifier = Modifier,
    todo: Todo,
    onDeleteClicked: () -> Unit,
) {
    IconButton(onClick = { onDeleteClicked.invoke() }, modifier = modifier) {
        androidx.wear.compose.material.Icon(
            painter = if (todo.dueDate == null) painterResource(id = R.drawable.ic_not_scheduled) else painterResource(
                id = R.drawable.ic_scheduled
            ),
            contentDescription = stringResource(id = R.string.schedule_icon),
            tint = if (todo.prioritized) Color(0xFFFFFFFF) else Color(0xFF006E1C)
        )
    }
}
