package com.engineerfred.kotlin.todoapp.feature_todo.presentation.screens.todo_list_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.engineerfred.kotlin.todoapp.R
import com.engineerfred.kotlin.todoapp.core.util.Constants.EMPTY_SERVER_RESPONSE_EXCEPTION
import com.engineerfred.kotlin.todoapp.core.util.Constants.NO_INTERNET_EXCEPTION
import com.engineerfred.kotlin.todoapp.core.util.Resource
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.screens.todo_list_screen.components.DrawerHeader
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.screens.todo_list_screen.components.MenuDrawerContent
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.screens.todo_list_screen.components.TasksList
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.view_models.todos_list_view_model.TodosListEvents
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.view_models.todos_list_view_model.TodosListViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodosListScreen(
    onCreateTask: () -> Unit,
    onUpdateTask: ( todoId:Long ) -> Unit,
    onSearchClicked: () -> Unit
) {

    val todosListViewModel = hiltViewModel<TodosListViewModel>()

    val bgColor =  if ( todosListViewModel.uiState.isLightTheme )  {
        Color(0xFF0061A4)
    } else {
        Color(0xFF1A1C1E)
    }

    val todosState = todosListViewModel.todosState.collectAsState().value

    val snackbarHostState = remember { SnackbarHostState() }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT
    val screenBackgroundImage = if ( isPortrait ) painterResource(id = R.drawable.background_portrait) else painterResource(id = R.drawable.background_landscape)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when( todosState ) {
            Resource.Loading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = screenBackgroundImage,
                        contentDescription = stringResource(id = R.string.screen_background_image),
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier.fillMaxSize(),
                        alignment = Alignment.TopStart
                    )
                    Text(
                        text = "Loading...",
                        Modifier
                            .padding(20.dp)
                            .align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 15.sp
                    )
                }
            }
            is Resource.Success -> {
                val todos = todosState.result

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet {
                            DrawerHeader()
                            Text(
                                text = "Sort by:",
                                modifier = Modifier.padding(top = 16.dp, start=30.dp, bottom= 16.dp),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Divider()
                            MenuDrawerContent(
                                todosOrder = todosListViewModel.uiState.todosOrder,
                                onOrderChange = {
                                    todosListViewModel.onEvent( TodosListEvents.TodosSortClicked(it) )
                                },
                                modifier = Modifier.weight(1f),
                                isLightTheme = todosListViewModel.uiState.isLightTheme,
                                onThemeChange = {
                                    todosListViewModel.onEvent( TodosListEvents.OnThemeChanged )
                                }
                            )
                            Text(
                                text = "Developed by Engineer Fred @2024",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                textAlign = TextAlign.Center,
                                color = Color.Gray,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                ) {
                    Scaffold(
                        floatingActionButton = {
                            FloatingActionButton(
                                onClick = { onCreateTask.invoke() },
                                shape = CircleShape,
                                containerColor = bgColor
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_add),
                                    contentDescription = stringResource(id = R.string.add_todo_icon),
                                    tint = Color(0xFFE2E2E6)
                                )
                            }
                        },
                        topBar = {
                            TopAppBar(
                                title = {
                                    Box{
                                        Text(
                                            text = "Tasks",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 22.sp,
                                            modifier = Modifier
                                                .padding(end = 20.dp)
                                                .align(Alignment.CenterStart),
                                            color = Color(0xFFE2E2E6)
                                        )
                                        Text(
                                            text = "${todos.size}",
                                            fontSize = 12.sp,
                                            modifier = Modifier.align(Alignment.TopEnd),
                                            color = Color(0xFFE2E2E6)
                                        )
                                    }
                                },
                                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                    containerColor = bgColor,
                                    navigationIconContentColor = Color(0xFFE2E2E6),
                                    titleContentColor = Color(0xFFE2E2E6),
                                    actionIconContentColor = Color(0xFFE2E2E6)
                                ),
                                navigationIcon = {
                                    IconButton(onClick = {
                                        scope.launch {
                                            drawerState.open()
                                        }
                                    }) {
                                        if ( drawerState.isClosed ) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_menu_closed),
                                                contentDescription =  stringResource(id = R.string.open_drawer_menu_icon)
                                            )
                                        }else {
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_menu_opened),
                                                contentDescription =  stringResource(id = R.string.close_drawer_menu_icon)
                                            )
                                        }
                                    }
                                },
                                actions = {
                                    IconButton(onClick = {
                                        onSearchClicked.invoke()
                                    }) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.baseline_search_24),
                                            contentDescription =  stringResource(id = R.string.search_icon)
                                        )
                                    }
                                }
                            )
                        },
                        snackbarHost = { SnackbarHost( hostState = snackbarHostState ) }
                    ) {

                        Box(modifier = Modifier.fillMaxSize()) {
                            Image(
                                painter = screenBackgroundImage,
                                contentDescription = stringResource(id = R.string.screen_background_image),
                                contentScale = ContentScale.FillWidth,
                                modifier = Modifier.fillMaxSize(),
                                alignment = Alignment.TopStart
                            )
                            TasksList(
                                todos = todos,
                                todosListViewModel = todosListViewModel,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 66.dp),
                                onPrioritize = {
                                    todosListViewModel.onEvent( TodosListEvents.TodoPrioritized(it) )
                                }, onCompleted = {
                                    todosListViewModel.onEvent( TodosListEvents.TodoCompletedClicked(it) )
                                }, onDeleted = {
                                    todosListViewModel.onEvent( TodosListEvents.DeleteTodoClicked(it) )
                                }, onCardClicked = {
                                    onUpdateTask.invoke(it)
                                }, isDarkTheme = todosListViewModel.uiState.isLightTheme
                            )
                        }
                    }
                }
            }

            is Resource.Failure -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = screenBackgroundImage,
                        contentDescription = stringResource(id = R.string.screen_background_image),
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier.fillMaxSize(),
                        alignment = Alignment.TopStart
                    )
                    
                    if ( todosState.errorMessage == EMPTY_SERVER_RESPONSE_EXCEPTION ) {
                        Text(
                            text = todosState.errorMessage,
                            Modifier
                                .padding(20.dp)
                                .align(Alignment.Center)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color(0xFF0061A4)
                        )
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .fillMaxWidth()
                                .padding(26.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            FloatingActionButton(
                                onClick = { onCreateTask.invoke() },
                                shape = CircleShape,
                                containerColor = MaterialTheme.colorScheme.primary
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_add),
                                    contentDescription = stringResource(id = R.string.save_todo_icon),
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    } else if ( todosState.errorMessage == NO_INTERNET_EXCEPTION ) {
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .fillMaxWidth()
                                .padding(20.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = todosState.errorMessage,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                fontSize = 17.sp ,
                                color = Color(0xFF0061A4)
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            OutlinedButton(onClick = { todosListViewModel.onEvent( TodosListEvents.RetryClicked ) }) {
                                Text(text = "Try again")
                            }
                        }
                    } else {
                        Text(
                            text = todosState.errorMessage,
                            Modifier
                                .padding(20.dp)
                                .align(Alignment.Center)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = Color(0xFFBA1A1A)
                        )
                    }
                }
            }
            Resource.Undefined -> Unit
        }
    }
}