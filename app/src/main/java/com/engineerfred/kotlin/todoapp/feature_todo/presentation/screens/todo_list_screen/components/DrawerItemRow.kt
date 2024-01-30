package com.engineerfred.kotlin.todoapp.feature_todo.presentation.screens.todo_list_screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.engineerfred.kotlin.todoapp.R
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.theme.ToDoAppTheme

@Composable
fun DrawerItemRow(
    modifier: Modifier = Modifier,
    text: String,
    isChecked: Boolean
) {

    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp, top = 10.dp)
    ) {
        Text( text = text, modifier.weight(1f) )
        AnimatedVisibility(visible = isChecked) {
            Icon(
                painter = painterResource(id = R.drawable.ic_done ), 
                contentDescription = stringResource(
                id = R.string.drawer_item_selected_icon),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Preview( showBackground = true )
@Composable
fun DrawerMenuItemRow() {
    ToDoAppTheme {
        DrawerItemRow(text = "Completed", isChecked = true)
    }
}