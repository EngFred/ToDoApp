package com.engineerfred.kotlin.todoapp.feature_todo.presentation.screens.todo_list_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import com.engineerfred.kotlin.todoapp.core.util.Resource
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.screens.todo_list_screen.components.DrawerHeader
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.screens.todo_list_screen.components.MenuDrawerContent
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.screens.todo_list_screen.components.TodosList
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.view_models.todos_list_view_model.TodosListEvents
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.view_models.todos_list_view_model.TodosListViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodosScreen() {

    val todosListViewModel = hiltViewModel<TodosListViewModel>()

    val todosState = todosListViewModel.todosState.collectAsState().value
    
    val snackbarHostState = remember { SnackbarHostState() }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT
    val screenBackgroundImage = if ( isPortrait ) painterResource(id = R.drawable.background_portrait) else painterResource(id = R.drawable.background_landscape)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
                    //CircularProgressIndicator( modifier = Modifier.size(28.dp) )
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
                                modifier = Modifier.padding(top = 16.dp, start=30.dp, bottom= 16.dp,),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Divider()
                            MenuDrawerContent(
                                todosOrder = todosListViewModel.uiState.todosOrder,
                                onOrderChange = {
                                    todosListViewModel.onEvent( TodosListEvents.TodosSortClicked(it) )
                                },
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "Developed by Engineer Fred @2024",
                                modifier = Modifier.fillMaxWidth().padding(20.dp),
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
                                onClick = { /*TODO*/ },
                                shape = CircleShape,
                                containerColor = MaterialTheme.colorScheme.primary
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_add),
                                    contentDescription = stringResource(id = R.string.add_todo_icon),
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        },
                        topBar = {
                            CenterAlignedTopAppBar(
                                title = {
                                    Box{
                                        Text(
                                            text = "Tasks",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 22.sp,
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            modifier = Modifier
                                                .padding(end = 20.dp)
                                                .align(Alignment.CenterStart)
                                        )
                                        Text(
                                            text = "${todos.size}",
                                            fontSize = 12.sp,
                                            modifier = Modifier.align(
                                            Alignment.TopEnd)
                                        )
                                    }
                                },
                                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = .7f),
                                    scrolledContainerColor = MaterialTheme.colorScheme.primary,
                                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                                ),
                                navigationIcon = {},
                                actions = {
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
                                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
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
                            TodosList(
                                todos = todos,
                                todosListViewModel = todosListViewModel,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 66.dp)
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
                    Text(
                        text = todosState.errorMessage,
                        Modifier
                            .padding(20.dp)
                            .align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            Resource.Undefined -> Unit
        }
    }

}