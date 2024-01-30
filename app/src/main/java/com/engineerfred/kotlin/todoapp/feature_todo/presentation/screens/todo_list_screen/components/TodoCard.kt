package com.engineerfred.kotlin.todoapp.feature_todo.presentation.screens.todo_list_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.engineerfred.kotlin.todoapp.core.presentation.components.AchieveButton
import com.engineerfred.kotlin.todoapp.core.presentation.components.CompleteButton
import com.engineerfred.kotlin.todoapp.core.presentation.components.DeleteButton
import com.engineerfred.kotlin.todoapp.core.presentation.components.getTodoColors
import com.engineerfred.kotlin.todoapp.feature_todo.domain.models.Todo
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.theme.ToDoAppTheme
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.util.formatTodoTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoCard(
    modifier: Modifier = Modifier,
    todo: Todo,
    onDeleteClicked: () -> Unit,
    onAchieveClicked: () -> Unit,
    onCardClicked: () -> Unit,
    onCompletedClicked: () -> Unit,
) {
    val todoColors = getTodoColors(todo = todo)
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
        shape = RoundedCornerShape(24.dp),
        onClick = { onCardClicked.invoke() },
        colors = CardDefaults.cardColors(todoColors.backgroundColor)
    ) {

        Column(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        ) {
            Row(
                modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CompleteButton(onCompleteClicked = {onCompletedClicked.invoke()}, completed = todo.completed, color = todoColors.completedIconColor)
                Text(text = todo.title, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis, fontSize = 18.sp, color = todoColors.textColor)
            }
            Text(text = todo.description,
                modifier.fillMaxWidth()
                    .padding(horizontal = 15.dp),
                textAlign = TextAlign.Start,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = todoColors.textColor
            )
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = formatTodoTime(todo.timeStamp), modifier.weight(1f).padding(start = 19.dp), fontSize = 12.sp, color = Color.DarkGray)
                AchieveButton(onAchieveClicked = { onAchieveClicked.invoke() }, achieved = todo.archived, color = todoColors.achieveIconColor )
                DeleteButton( onDeleteClicked = onDeleteClicked )
            }
        }
    }
}


@Preview( showBackground = true )
@Composable
fun TodoCardPreview() {
    ToDoAppTheme {
        TodoCard(
            todo = Todo(
                id = 100,
                title = "Learn KMM bla bla bla blah bla bla bla bla blah bla bla bla bla blah ",
                description = "bla bla bla blah bla bla bla bla blah bla bla bla bla blah bla bla bla bla blah bla",
                timeStamp = 0L,
                completed = false,
                archived = true
            ),
            onDeleteClicked = { /*TODO*/ },
            onAchieveClicked = { /*TODO*/ },
            onCardClicked = { /*TODO*/ }) {

        }
    }
}