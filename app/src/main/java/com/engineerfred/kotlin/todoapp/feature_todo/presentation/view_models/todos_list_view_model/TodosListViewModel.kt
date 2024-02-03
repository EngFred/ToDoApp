package com.engineerfred.kotlin.todoapp.feature_todo.presentation.view_models.todos_list_view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.engineerfred.kotlin.todoapp.core.util.Resource
import com.engineerfred.kotlin.todoapp.feature_todo.data.di.IoDispatcher
import com.engineerfred.kotlin.todoapp.feature_todo.data.mappers.toTodoEntity
import com.engineerfred.kotlin.todoapp.feature_todo.domain.models.TasksOrderPreferences
import com.engineerfred.kotlin.todoapp.feature_todo.domain.models.Todo
import com.engineerfred.kotlin.todoapp.feature_todo.domain.use_cases.AddTodoUseCase
import com.engineerfred.kotlin.todoapp.feature_todo.domain.use_cases.ChangeAppThemeUseCase
import com.engineerfred.kotlin.todoapp.feature_todo.domain.use_cases.ChangeTasksOrderUseCase
import com.engineerfred.kotlin.todoapp.feature_todo.domain.use_cases.DeleteTodoUseCase
import com.engineerfred.kotlin.todoapp.feature_todo.domain.use_cases.GetAllTodosUseCase
import com.engineerfred.kotlin.todoapp.feature_todo.domain.use_cases.GetAppThemeUseCase
import com.engineerfred.kotlin.todoapp.feature_todo.domain.use_cases.GetTasksOrderUseCase
import com.engineerfred.kotlin.todoapp.feature_todo.domain.use_cases.UpdateTodoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TodosListViewModel @Inject constructor(
    private val getAllTodosUseCase: GetAllTodosUseCase,
    private val updateTodoUseCase: UpdateTodoUseCase,
    private val deleteTodoUseCase: DeleteTodoUseCase,
    private val addTodoUseCase: AddTodoUseCase,
    private val getAppThemeUseCase: GetAppThemeUseCase,
    private val changeAppThemeUseCase: ChangeAppThemeUseCase,
    private val changeTasksOrderUseCase: ChangeTasksOrderUseCase,
    private val getTasksOrderUseCase: GetTasksOrderUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _todosState = MutableStateFlow<Resource<List<Todo>>>(Resource.Loading)
    val todosState = _todosState.asStateFlow()

    private val _todoDeleteState = MutableStateFlow<Resource<Unit>>(Resource.Undefined)
    val todoDeleteState = _todoDeleteState.asStateFlow()

    private val _todoUpdateState = MutableStateFlow<Resource<Unit?>>(Resource.Undefined)
    val todoUpdateState = _todoUpdateState.asStateFlow()

    var uiState by mutableStateOf(TodosListScreenState())
        private set

    private var undoTodoDelete: Todo? = null
    private var todoJob: Job? = null

    init {
        initialize()
    }


    fun onEvent( event: TodosListEvents ) {
        when( event ) {
            is TodosListEvents.DeleteTodoClicked -> {
                undoTodoDelete = event.todo
                viewModelScope.launch {
                    deleteTodoUseCase.invoke( event.todo ).collect{
                        _todoDeleteState.value = it
                    }
                }
            }
            is TodosListEvents.TodoCompletedClicked -> {
                viewModelScope.launch{
                    updateTodoUseCase.invoke(event.todo.copy( completed = !event.todo.completed )).collect{
                        _todoUpdateState.value = it
                    }
                }
            }
            is TodosListEvents.TodosSortClicked -> {
                uiState = uiState.copy( todosOrder = event.todosOrder  )
                saveTasksOrder( uiState.todosOrder )
            }

            is TodosListEvents.UndoTodoDeleteClicked -> {
                viewModelScope.launch{
                    addTodoUseCase.invoke( undoTodoDelete?.toTodoEntity() ?: return@launch )
                    undoTodoDelete = null
                }
            }
            is TodosListEvents.TodoArchived -> {
                _todoUpdateState.value = Resource.Loading
                viewModelScope.launch {
                    updateTodoUseCase.invoke(event.todo.copy( archived = !event.todo.archived )).collect{
                        _todoUpdateState.value = it
                    }
                }
            }
            is TodosListEvents.OnThemeChanged -> {
                uiState = uiState.copy( isLightTheme =  !uiState.isLightTheme )
                changeAppTheme()
            }
        }
    }


    private fun initialize() {
        getAppTheme()
        getTasksOrder()
        getAllTodos()
    }

    private fun getTasksOrder() {
        viewModelScope.launch {
            var tasksOrder = uiState.todosOrder
            getTasksOrderUseCase.invoke().collect{
                when (it.sortingDirection) {
                    "a_z" -> {
                        when (it.sortingType) {
                            "time" -> {
                                tasksOrder = when {
                                    tasksOrder.showAchieved -> {
                                        TodosOrder.Time( TodosSortingDirection.AtoZ, true )
                                    } else -> TodosOrder.Time( TodosSortingDirection.AtoZ, false)
                                }
                            }
                            "title" -> {
                                tasksOrder = when {
                                    it.showAchieved -> {
                                        TodosOrder.Title( TodosSortingDirection.AtoZ, true )
                                    } else -> TodosOrder.Title( TodosSortingDirection.AtoZ, false)
                                }
                            }
                            "completed" -> {
                                tasksOrder = when {
                                    it.showAchieved -> {
                                        TodosOrder.Completed( TodosSortingDirection.AtoZ, true )
                                    } else -> TodosOrder.Completed( TodosSortingDirection.AtoZ, false)
                                }
                            }
                        }
                    }
                    "z_a" -> {
                        when (it.sortingType) {
                            "time" -> {
                                tasksOrder = when {
                                    it.showAchieved -> {
                                        TodosOrder.Time( TodosSortingDirection.ZtoA, true )
                                    } else -> TodosOrder.Time( TodosSortingDirection.ZtoA, false)
                                }
                            }
                            "title" -> {
                                tasksOrder = when {
                                    it.showAchieved -> {
                                        TodosOrder.Title( TodosSortingDirection.ZtoA, true )
                                    } else -> TodosOrder.Title( TodosSortingDirection.ZtoA, false)
                                }
                            }
                            "completed" -> {
                                tasksOrder = when {
                                    it.showAchieved -> {
                                        TodosOrder.Completed( TodosSortingDirection.ZtoA, true )
                                    } else -> TodosOrder.Completed( TodosSortingDirection.ZtoA, false)
                                }
                            }
                        }
                    }
                }
                uiState = uiState.copy( todosOrder = tasksOrder )
            }
        }
    }


    private fun changeAppTheme() {
        viewModelScope.launch( ioDispatcher ) {
            changeAppThemeUseCase.invoke( uiState.isLightTheme )
        }
    }

    private fun getAppTheme() {
        viewModelScope.launch {
            getAppThemeUseCase.invoke().collect{
                uiState = uiState.copy( isLightTheme = it )
            }
        }
    }

    private fun getAllTodos() {
        todoJob?.cancel()
        todoJob = viewModelScope.launch {
            getAllTodosUseCase.invoke().collect{
                _todosState.value = it
            }
        }
    }

    private fun saveTasksOrder( todosOrder: TodosOrder ) {
        val tasksOrderPreferences = TasksOrderPreferences(
            sortingType = when (todosOrder) {
                is TodosOrder.Title -> "title"
                is TodosOrder.Time -> "time"
                is TodosOrder.Completed -> "completed"
            },
            sortingDirection = when( todosOrder.sortingDirection ) {
                TodosSortingDirection.AtoZ -> "a_z"
                TodosSortingDirection.ZtoA -> "z_a"
            },
            showAchieved = todosOrder.showAchieved
        )
        viewModelScope.launch {
            changeTasksOrderUseCase.invoke(tasksOrderPreferences)
        }
    }
}



//                val stateOrderAlreadyMatchesEventOrder = uiState.todosOrder::class == event.todosOrder::class &&
//                        uiState.todosOrder.showAchieved == event.todosOrder.showAchieved &&
//                        uiState.todosOrder.sortingDirection == event.todosOrder.sortingDirection
//                if( stateOrderAlreadyMatchesEventOrder ) return else {
//                    uiState = uiState.copy( todosOrder = event.todosOrder  )
//                }