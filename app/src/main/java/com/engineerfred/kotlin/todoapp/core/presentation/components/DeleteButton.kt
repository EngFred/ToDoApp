package com.engineerfred.kotlin.todoapp.core.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.engineerfred.kotlin.todoapp.R

@Composable
fun DeleteButton(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF690005),
    onDeleteClicked: () -> Unit,
) {
    //TODO("On Deleting todos should move to the trash for 30 days")

    IconButton(onClick = { onDeleteClicked.invoke() }, modifier = modifier) {
        Icon(
            painter = painterResource(id = R.drawable.ic_delete),
            contentDescription = stringResource(id = R.string.todo_delete_icon),
            tint = color,
            modifier = modifier.size(28.dp),
        )
    }
}
