package com.engineerfred.kotlin.todoapp.feature_todo.presentation.screens.todo_new_update_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.engineerfred.kotlin.todoapp.R
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.screens.todo_new_update_screen.components.MyDatePickerDialog
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.screens.todo_new_update_screen.components.SaveUpdateTodoBottomRow
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.screens.todo_new_update_screen.components.SaveUpdateTodoTopRow
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.util.formatTodoTime
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.view_models.save_update_todo_view_model.SaveUpdateTodoScreenEvents
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.view_models.save_update_todo_view_model.SaveUpdateTodoViewModel
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.view_models.save_update_todo_view_model.SaveUpdateTodoViewModelAssistedFactory
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.view_models.save_update_todo_view_model.SaveUpdateTodoViewModelFactory
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.view_models.todos_list_view_model.TodosListViewModel

@Composable
fun SaveUpdateTodoScreen(
    saveUpdateTodoViewModelAssistedFactory: SaveUpdateTodoViewModelAssistedFactory,
    onBackClicked: () -> Unit,
    todoId: Long
) {

    val saveUpdateTodoViewModel = viewModel(
        modelClass = SaveUpdateTodoViewModel::class.java,
        factory = SaveUpdateTodoViewModelFactory(
            saveUpdateTodoViewModelAssistedFactory, todoId
        )
    )

    val todosListViewModel = hiltViewModel<TodosListViewModel>()

    val bgColor =  if ( todosListViewModel.uiState.isLightTheme )  {
        Color(0xFF0061A4)
    } else {
        Color(0xFF1A1C1E)
    }

    var showDatePicker by remember {
        mutableStateOf(false)
    }

    if (showDatePicker) {
        MyDatePickerDialog(
            onDateSelected = {
                if ( it != 0L ) {
                    saveUpdateTodoViewModel.onEvent( SaveUpdateTodoScreenEvents.DueDateSelected(it) )
                }
            },
            onDismiss = { showDatePicker = false }
        )
    }

    if (saveUpdateTodoViewModel.saveUpdateCompleted) onBackClicked.invoke()

    val configuration = LocalConfiguration.current
    val localFM = LocalFocusManager.current
    val isPortrait =
        configuration.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT
    val screenBackgroundImage =
        if (isPortrait) painterResource(id = R.drawable.background_portrait) else painterResource(id = R.drawable.background_landscape)

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = screenBackgroundImage,
            contentDescription = stringResource(id = R.string.screen_background_image),
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxSize(),
            alignment = Alignment.TopStart
        )
        Column(
            Modifier.fillMaxSize()
        ) {
            SaveUpdateTodoTopRow(
                title = if (todoId == -1L) "Create Task" else "Update Task",
                onBackClicked = onBackClicked,
                bgColor = bgColor
            )
            Column(
                Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top
            ) {
                TextField(
                    value = saveUpdateTodoViewModel.uiState.todo.title,
                    onValueChange = {
                        saveUpdateTodoViewModel.onEvent(SaveUpdateTodoScreenEvents.TitleChanged(it))
                    },
                    placeholder = { Text(text = "Todo title..", fontSize = 28.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions { localFM.clearFocus() },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent, // Remove underline when focused
                        unfocusedIndicatorColor = Color.Transparent, // Remove underline when unfocused
                        disabledIndicatorColor = Color.Transparent, // Remove underline when disabled
                    ), textStyle = TextStyle.Default.copy(fontSize = 28.sp)
                )
                Spacer(modifier = Modifier.size(5.dp))
                AnimatedVisibility(saveUpdateTodoViewModel.uiState.titleErrorMessage.isNotEmpty()) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            imageVector = Icons.Default.ErrorOutline,
                            contentDescription = stringResource(id = R.string.error_icon),
                            modifier = Modifier.size(17.dp),
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = saveUpdateTodoViewModel.uiState.titleErrorMessage,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(horizontal = 5.dp),
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Spacer(modifier = Modifier.size(5.dp))
                TextField(
                    value = saveUpdateTodoViewModel.uiState.todo.description,
                    onValueChange = {
                        saveUpdateTodoViewModel.onEvent(
                            SaveUpdateTodoScreenEvents.DescriptionChanged(it)
                        )
                    },
                    placeholder = { Text(text = "Todo description..", fontSize = 24.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 10,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions { localFM.clearFocus() },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent, // Remove underline when focused
                        unfocusedIndicatorColor = Color.Transparent, // Remove underline when unfocused
                        disabledIndicatorColor = Color.Transparent, // Remove underline when disabled
                    ), textStyle = TextStyle.Default.copy(fontSize = 24.sp)
                )

                Spacer(modifier = Modifier.size(5.dp))
                AnimatedVisibility(visible = saveUpdateTodoViewModel.uiState.descriptionErrorMessage.isNotEmpty()) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            imageVector = Icons.Default.ErrorOutline,
                            contentDescription = stringResource(id = R.string.error_icon),
                            modifier = Modifier.size(17.dp),
                            tint =  MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = saveUpdateTodoViewModel.uiState.descriptionErrorMessage,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 5.dp),
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Spacer(modifier = Modifier.size(5.dp))
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = if ( saveUpdateTodoViewModel.uiState.dueDate == null ) "Schedule task" else "Scheduled date:",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF8B5000),
                        modifier = Modifier.padding(start = 5.dp)
                    )
                    Button(
                        onClick = { showDatePicker = true },
                        shape = RoundedCornerShape(5.dp)
                    ) {
                        Text(
                            text = if (saveUpdateTodoViewModel.uiState.dueDate == null)
                                "Choose a date"
                            else
                                formatTodoTime( saveUpdateTodoViewModel.uiState.dueDate ))
                    }
                }
                Spacer(modifier = Modifier.size(5.dp))
                AnimatedVisibility(visible = saveUpdateTodoViewModel.uiState.invalidDateMessage.isNotEmpty()) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            imageVector = Icons.Default.ErrorOutline,
                            contentDescription = stringResource(id = R.string.error_icon),
                            modifier = Modifier.size(17.dp),
                            tint =  MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = saveUpdateTodoViewModel.uiState.invalidDateMessage,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 5.dp),
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
            SaveUpdateTodoBottomRow(
                modifier = Modifier,
                todo = saveUpdateTodoViewModel.uiState.todo,
                onCompleted = {
                    saveUpdateTodoViewModel.onEvent(SaveUpdateTodoScreenEvents.CompletedClicked)
                },
                onAchieved = {
                    saveUpdateTodoViewModel.onEvent(SaveUpdateTodoScreenEvents.AchieveClicked)
                },
                onDeleted = {
                    saveUpdateTodoViewModel.onEvent(SaveUpdateTodoScreenEvents.DeleteClicked)
                },
                onSave = {
                    saveUpdateTodoViewModel.onEvent(SaveUpdateTodoScreenEvents.SaveClicked)
                }, bgColor = bgColor
            )
        }
    }
}

//@Preview( showBackground = true )
//@Composable
//fun TodoNewUpdateScreenPreview() {
//    ToDoAppTheme {
//        SaveUpdateTodoScreen(onBackClicked = { /*TODO*/ }, todoId = 2)
//    }
//}