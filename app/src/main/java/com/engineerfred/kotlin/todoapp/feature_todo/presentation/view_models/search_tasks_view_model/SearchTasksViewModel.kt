package com.engineerfred.kotlin.todoapp.feature_todo.presentation.view_models.search_tasks_view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.engineerfred.kotlin.todoapp.feature_todo.data.di.IoDispatcher
import com.engineerfred.kotlin.todoapp.feature_todo.domain.models.Todo
import com.engineerfred.kotlin.todoapp.feature_todo.domain.use_cases.DeleteTodoUseCase
import com.engineerfred.kotlin.todoapp.feature_todo.domain.use_cases.SearchTasksUseCase
import com.engineerfred.kotlin.todoapp.feature_todo.domain.use_cases.UpdateTodoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchTasksViewModel @Inject constructor(
    private val searchTasksUseCase: SearchTasksUseCase,
    private val updateTodoUseCase: UpdateTodoUseCase,
    private val deleteTodoUseCase: DeleteTodoUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): ViewModel() {
    var uiState by mutableStateOf(SearchTasksScreenState())
        private set

    private val _searchResults = MutableStateFlow<List<Todo>>(emptyList())
    val searchResults = _searchResults.asStateFlow()


    fun onEvent(  event: SearchTasksScreenEvents) {
        when(event) {
            is SearchTasksScreenEvents.OnQueryChanged -> {
                uiState = uiState.copy( searchQuery = event.query )
                searchTasks( uiState.searchQuery.lowercase() )
            }
            SearchTasksScreenEvents.ClearClicked -> {
                if ( uiState.searchQuery.isNotEmpty() )
                    uiState = uiState.copy( searchQuery = "" )
            }
            is SearchTasksScreenEvents.DeleteTodoClicked -> {
                viewModelScope.launch( ioDispatcher ) {
                    deleteTodoUseCase.invoke( event.todo )
                }
            }
            is SearchTasksScreenEvents.TaskCompletedClicked -> {
                viewModelScope.launch( ioDispatcher ){
                    updateTodoUseCase.invoke(event.todo.copy( completed = !event.todo.completed ))
                }
            }
            is SearchTasksScreenEvents.TaskPrioritized -> {
                viewModelScope.launch( ioDispatcher ) {
                    updateTodoUseCase.invoke(event.todo.copy( prioritized = !event.todo.prioritized ))
                }
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun searchTasks(query: String ) {
        if ( query.isNotEmpty() ) {
            viewModelScope.launch {
                searchTasksUseCase.invoke( query )
                    .debounce(300)
                    .collect {
                        _searchResults.value = it
                    }
            }
        }
    }
}