package com.engineerfred.kotlin.todoapp.feature_todo.presentation.view_models.todos_list_view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.engineerfred.kotlin.todoapp.core.util.Resource
import com.engineerfred.kotlin.todoapp.feature_todo.data.di.IoDispatcher
import com.engineerfred.kotlin.todoapp.feature_todo.domain.models.Todo
import com.engineerfred.kotlin.todoapp.feature_todo.domain.use_cases.AddTodoUseCase
import com.engineerfred.kotlin.todoapp.feature_todo.domain.use_cases.DeleteTodoUseCase
import com.engineerfred.kotlin.todoapp.feature_todo.domain.use_cases.GetAllTodosUseCase
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
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _todosState = MutableStateFlow<Resource<List<Todo>>>(Resource.Loading)
    val todosState = _todosState.asStateFlow()

    private val _todoDeleteState = MutableStateFlow<Resource<Unit>?>(null)
    val todoDeleteState = _todoDeleteState.asStateFlow()

    private val _todoUpdateState = MutableStateFlow<Resource<Unit>?>(null)
    val todoUpdateState = _todoUpdateState.asStateFlow()

    var uiState by mutableStateOf(TodosListState())
        private set

    private var undoTodoDelete: Todo? = null
    private var todoJob: Job? = null

    init {
        getAllTodos()
    }


    fun onEvent( event: TodosListEvents ) {
        when( event ) {
            is TodosListEvents.DeleteTodoClicked -> {
                undoTodoDelete = event.todo
                viewModelScope.launch(ioDispatcher) {
                    _todoDeleteState.value = deleteTodoUseCase.invoke( event.todo )
                }
            }
            is TodosListEvents.TodoCompletedClicked -> {
                viewModelScope.launch(ioDispatcher) {
                    _todoUpdateState.value = updateTodoUseCase.invoke(event.todo.copy( completed = !event.todo.completed ))
                }
            }
            is TodosListEvents.TodosSortClicked -> {
                val stateOrderAlreadyMatchesEventOrder = uiState.todosOrder::class == event.todosOrder::class &&
                        uiState.todosOrder.showAchieved == event.todosOrder.showAchieved &&
                        uiState.todosOrder.sortingDirection == event.todosOrder.sortingDirection
                if( stateOrderAlreadyMatchesEventOrder ) return else {
                    uiState = uiState.copy( todosOrder = event.todosOrder  )
                }
            }

            is TodosListEvents.UndoTodoDeleteClicked -> {
                viewModelScope.launch(ioDispatcher) {
                    addTodoUseCase.invoke( undoTodoDelete ?: return@launch )
                    undoTodoDelete = null
                }
            }
            is TodosListEvents.TodoArchived -> {
                viewModelScope.launch(ioDispatcher) {
                    _todoUpdateState.value = updateTodoUseCase.invoke(event.todo.copy( archived = !event.todo.archived ))
                }
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

}