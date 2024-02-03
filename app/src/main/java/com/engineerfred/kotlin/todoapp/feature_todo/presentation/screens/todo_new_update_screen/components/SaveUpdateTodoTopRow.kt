package com.engineerfred.kotlin.todoapp.feature_todo.presentation.screens.todo_new_update_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.engineerfred.kotlin.todoapp.R
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.theme.ToDoAppTheme

@Composable
fun SaveUpdateTodoTopRow(
    modifier: Modifier = Modifier,
    title: String,
    onBackClicked: () -> Unit,
    bgColor: Color
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(bgColor.copy(alpha = .7f))
            .padding(end = 46.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(onClick = { onBackClicked.invoke() }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = stringResource(id = R.string.back_icon),
                tint = Color(0xFFFDFCFF)
            )
        }

        Text(
            text = title,
            modifier.weight(1f),
            textAlign = TextAlign.Center,
            fontSize = 22.sp,
            color = Color(0xFFFDFCFF)
        )
    }

}

@Preview( showBackground = true )
@Composable
fun TopRowPreview() {
    ToDoAppTheme {
        SaveUpdateTodoTopRow(title = "Create todo", onBackClicked = {
        }, bgColor = Color.Blue)
    }
}