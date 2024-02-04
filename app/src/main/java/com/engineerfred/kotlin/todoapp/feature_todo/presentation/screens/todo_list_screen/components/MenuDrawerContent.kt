package com.engineerfred.kotlin.todoapp.feature_todo.presentation.screens.todo_list_screen.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.engineerfred.kotlin.todoapp.R
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.view_models.todos_list_view_model.TodosOrder
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.view_models.todos_list_view_model.TodosSortingDirection

@Composable
fun MenuDrawerContent(
    modifier: Modifier = Modifier,
    todosOrder: TodosOrder, //will be obtain from the uiState
    onOrderChange: (TodosOrder) -> Unit,
    isLightTheme: Boolean,
    onThemeChange: () -> Unit
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
                onOrderChange.invoke( TodosOrder.Title(todosOrder.sortingDirection, todosOrder.showPrioritized) )
            }
        )

        val sortByTime = todosOrder::class == TodosOrder.Time::class //for time sort selection
        NavigationDrawerItem(
            label = {
                DrawerItemRow(text = "Time", isChecked = sortByTime )
            },
            selected = false,
            onClick = {
                onOrderChange.invoke( TodosOrder.Time(todosOrder.sortingDirection, todosOrder.showPrioritized) )
            }
        )

        val sortByCompleted = todosOrder::class == TodosOrder.Completed::class //for complete sort selection
        NavigationDrawerItem(
            label = {
                DrawerItemRow(text = "Completed", isChecked = sortByCompleted )
            },
            selected = false,
            onClick = {
                onOrderChange.invoke( TodosOrder.Completed(todosOrder.sortingDirection, todosOrder.showPrioritized) )
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
                onOrderChange.invoke( todosOrder.update( TodosSortingDirection.AtoZ, todosOrder.showPrioritized ) )
            }
        )

        val sortDirectionZtoA = todosOrder.sortingDirection == TodosSortingDirection.ZtoA
        NavigationDrawerItem(
            label = {
                DrawerItemRow(text = "Z to A", isChecked = sortDirectionZtoA )
            },
            selected = false,
            onClick = {
                onOrderChange.invoke( todosOrder.update( TodosSortingDirection.ZtoA, todosOrder.showPrioritized ) )
            }
        )

        Spacer(modifier = Modifier.height(7.dp))
        Divider()

        NavigationDrawerItem(
            label = {
                DrawerItemRow(text = "Show Prioritized", isChecked = todosOrder.showPrioritized )
            },
            selected = false,
            onClick = {
                onOrderChange.invoke( todosOrder.update( todosOrder.sortingDirection, !todosOrder.showPrioritized ) )
            }
        )

        Spacer(modifier = Modifier.height(7.dp))
        Divider()

        Row(
            modifier  = modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 0.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Switch(
                checked = !isLightTheme, //whenever our boolean value from the state is false
                onCheckedChange = {
                    Log.v("SwitchBoolean", "$it")
                    onThemeChange.invoke()
                }
            )
            Text(
                text = if (isLightTheme) "Light Theme" else "Dark Theme", //whenever our boolean value from the state is false
                modifier = modifier.padding(start = 6.dp).weight(1f),
                fontSize = 14.sp
            )
            Icon(
                painter = if (isLightTheme) painterResource(id = R.drawable.ic_light_mode) else painterResource(id = R.drawable.ic__dark_mode_24),
                contentDescription = stringResource(id = R.string.theme_icon)
            )
        }
        
        Spacer(modifier = Modifier.weight(1f))

    }
}