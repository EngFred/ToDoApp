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
fun AchieveButton(
    modifier: Modifier = Modifier,
    onAchieveClicked: () -> Unit,
    achieved: Boolean,
    color: Color
) {

    IconButton(
        onClick = { onAchieveClicked.invoke() },
        modifier = modifier
    ) {
        if ( achieved ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_archive),
                contentDescription = stringResource(id = R.string.todo_achieved_icon),
                tint = color,
            )
        } else {
            Icon(
                painter = painterResource(id = R.drawable.ic_unarchive),
                contentDescription = stringResource(id = R.string.todo_not_achieved_icon),
                tint = color,
            )
        }
    }

}