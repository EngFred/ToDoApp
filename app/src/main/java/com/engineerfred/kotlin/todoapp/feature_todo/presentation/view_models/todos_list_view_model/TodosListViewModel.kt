package com.engineerfred.kotlin.todoapp.feature_todo.presentation.view_models.todos_list_view_model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.engineerfred.kotlin.todoapp.core.util.Resource
import com.engineerfred.kotlin.todoapp.feature_todo.data.mappers.toTodoEntity
import com.engineerfred.kotlin.todoapp.feature_todo.domain.models.Todo
import com.engineerfred.kotlin.todoapp.feature_todo.domain.use_cases.AddTodoUseCase
import com.engineerfred.kotlin.todoapp.feature_todo.domain.use_cases.DeleteTodoUseCase
import com.engineerfred.kotlin.todoapp.feature_todo.domain.use_cases.GetAllSavedToPostTasksUseCase
import com.engineerfred.kotlin.todoapp.feature_todo.domain.use_cases.GetAllTodosUseCase
import com.engineerfred.kotlin.todoapp.feature_todo.domain.use_cases.UpdateTodoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val getAllSavedToPostTasksUseCase: GetAllSavedToPostTasksUseCase
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
        getAllTodos()
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
//                val stateOrderAlreadyMatchesEventOrder = uiState.todosOrder::class == event.todosOrder::class &&
//                        uiState.todosOrder.showAchieved == event.todosOrder.showAchieved &&
//                        uiState.todosOrder.sortingDirection == event.todosOrder.sortingDirection
//                if( stateOrderAlreadyMatchesEventOrder ) return else {
//                    uiState = uiState.copy( todosOrder = event.todosOrder  )
//                }
                uiState = uiState.copy( todosOrder = event.todosOrder  )
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
        }
    }

    private fun getAllTodos() {
        todoJob?.cancel()
        todoJob = viewModelScope.launch {
            getAllTodosUseCase.invoke().collect{
                _todosState.value = it
            }
        }
        getAllSavedToPostTodos()
    }

    private fun getAllSavedToPostTodos() {
        viewModelScope.launch {
            getAllSavedToPostTasksUseCase.invoke().collect{
                if ( it is Resource.Success ) {
                    Log.v("SavedToPost", "Saved To Post todos in the todos model are ${it.result.size}")
                    Log.v("SavedToPost", "The first task in the 'Saved To Post' database table has a title ${it.result[0].title}")
                }
            }
        }
    }

}