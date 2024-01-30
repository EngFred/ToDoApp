package com.engineerfred.kotlin.todoapp.core.presentation.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.engineerfred.kotlin.todoapp.R

@Composable
fun CompleteButton(
    modifier: Modifier = Modifier,
    onCompleteClicked: () -> Unit,
    completed: Boolean,
    color: Color,
) {
    
    IconButton(onClick = { onCompleteClicked.invoke() }, modifier = modifier) {
        if ( completed ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_checked),
                contentDescription = stringResource(id = R.string.todo_completed_icon),
                tint = color,
//                modifier = modifier.size(48.dp)
            )
        } else {
            Icon(
                painter = painterResource(id = R.drawable.ic_unchecked),
                contentDescription = stringResource(id = R.string.todo_not_completed_icon),
                tint = color,
//                modifier = modifier.size(48.dp)
            )
        }
    }

}