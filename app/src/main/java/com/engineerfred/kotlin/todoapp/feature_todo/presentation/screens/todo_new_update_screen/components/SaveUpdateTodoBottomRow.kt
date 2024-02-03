package com.engineerfred.kotlin.todoapp.feature_todo.presentation.screens.todo_new_update_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.engineerfred.kotlin.todoapp.R
import com.engineerfred.kotlin.todoapp.core.presentation.components.AchieveButton
import com.engineerfred.kotlin.todoapp.core.presentation.components.CompleteButton
import com.engineerfred.kotlin.todoapp.core.presentation.components.DeleteButton
import com.engineerfred.kotlin.todoapp.core.util.getTodoColors
import com.engineerfred.kotlin.todoapp.feature_todo.domain.models.Todo
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.theme.ToDoAppTheme

@Composable
fun SaveUpdateTodoBottomRow(
    modifier: Modifier = Modifier,
    todo: Todo, //todo from the state
    onCompleted: () -> Unit,
    onAchieved: () -> Unit,
    onDeleted: () -> Unit,
    onSave: () -> Unit,
    bgColor: Color,
) {

    val todoColors = getTodoColors(todo = todo)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(todoColors.backgroundColor)
            .padding(end = 16.dp, top = 4.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CompleteButton(
            onCompleteClicked = onCompleted,
            completed = todo.completed ,
            color = todoColors.completedIconColor
        )
        AchieveButton(
            onAchieveClicked = onAchieved,
            achieved = todo.archived,
            color = todoColors.achieveIconColor
        )
        DeleteButton( onDeleteClicked = onDeleted )
        Spacer(modifier = Modifier.weight(1f))
        FloatingActionButton(
            onClick = { onSave.invoke() },
            shape = CircleShape,
            containerColor = bgColor
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_save),
                contentDescription = stringResource(id = R.string.save_todo_icon),
                tint = Color(0xFFFFFFFF)
            )
        }
    }
}

@Preview( showBackground = true )
@Composable
fun BottomRowPreview() {
    ToDoAppTheme {
        SaveUpdateTodoBottomRow(
            todo = Todo(
                id = 100,
                title = "Learn KMM bla bla bla blah bla bla bla bla blah bla bla bla bla blah ",
                description = "bla bla bla blah bla bla bla bla blah bla bla bla bla blah bla bla bla bla blah bla",
                timeStamp = 0L,
                completed = true,
                archived = true
            ),
            onCompleted = { /*TODO*/ },
            onAchieved = { /*TODO*/ },
            onDeleted = { /*TODO*/ },
            onSave = {},
            bgColor = Color.Blue
        )
    }
}