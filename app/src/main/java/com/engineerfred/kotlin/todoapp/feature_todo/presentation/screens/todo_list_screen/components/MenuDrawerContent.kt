package com.engineerfred.kotlin.todoapp.feature_todo.presentation.screens.todo_list_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Divider
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.view_models.todos_list_view_model.TodosOrder
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.view_models.todos_list_view_model.TodosSortingDirection

@Composable
fun MenuDrawerContent(
    modifier: Modifier = Modifier,
    todosOrder: TodosOrder, //will be obtain from the uiState
    onOrderChange: (TodosOrder) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        val sortByTitle = todosOrder::class == TodosOrder.Title::class //for title sort selection
        NavigationDrawerItem(
            label = {
                DrawerItemRow(text = "Title", isChecked = sortByTitle )
            },
            selected = false ,
            onClick = {
                onOrderChange.invoke( TodosOrder.Title(todosOrder.sortingDirection, todosOrder.showAchieved) )
            }
        )

        val sortByTime = todosOrder::class == TodosOrder.Time::class //for time sort selection
        NavigationDrawerItem(
            label = {
                DrawerItemRow(text = "Time", isChecked = sortByTime )
            },
            selected = false,
            onClick = {
                onOrderChange.invoke( TodosOrder.Time(todosOrder.sortingDirection, todosOrder.showAchieved) )
            }
        )

        val sortByCompleted = todosOrder::class == TodosOrder.Completed::class //for complete sort selection
        NavigationDrawerItem(
            label = {
                DrawerItemRow(text = "Completed", isChecked = sortByCompleted )
            },
            selected = false,
            onClick = {
                onOrderChange.invoke( TodosOrder.Completed(todosOrder.sortingDirection, todosOrder.showAchieved) )
            }
        )

        Spacer(modifier = Modifier.height(7.dp))
        Divider()

        val sortDirectionAtoZ = todosOrder.sortingDirection == TodosSortingDirection.AtoZ
        NavigationDrawerItem(
            label = {
                DrawerItemRow(text = "A to Z", isChecked = sortDirectionAtoZ )
            },
            selected = false,
            onClick = {
                onOrderChange.invoke( todosOrder.update( TodosSortingDirection.AtoZ, todosOrder.showAchieved ) )
            }
        )

        val sortDirectionZtoA = todosOrder.sortingDirection == TodosSortingDirection.ZtoA
        NavigationDrawerItem(
            label = {
                DrawerItemRow(text = "Z to A", isChecked = sortDirectionZtoA )
            },
            selected = false,
            onClick = {
                onOrderChange.invoke( todosOrder.update( TodosSortingDirection.ZtoA, todosOrder.showAchieved ) )
            }
        )

        Spacer(modifier = Modifier.height(7.dp))
        Divider()

        NavigationDrawerItem(
            label = {
                DrawerItemRow(text = "Show Achieved", isChecked = todosOrder.showAchieved )
            },
            selected = false,
            onClick = {
                onOrderChange.invoke( todosOrder.update( todosOrder.sortingDirection, !todosOrder.showAchieved ) )
            }
        )
    }
}