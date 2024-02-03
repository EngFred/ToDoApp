package com.engineerfred.kotlin.todoapp.feature_todo.presentation.view_models.save_update_todo_view_model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.engineerfred.kotlin.todoapp.core.util.Resource
import com.engineerfred.kotlin.todoapp.feature_todo.data.local.entities.TodoEntity
import com.engineerfred.kotlin.todoapp.feature_todo.domain.models.Todo
import com.engineerfred.kotlin.todoapp.feature_todo.domain.use_cases.AddTodoUseCase
import com.engineerfred.kotlin.todoapp.feature_todo.domain.use_cases.DeleteTodoUseCase
import com.engineerfred.kotlin.todoapp.feature_todo.domain.use_cases.GetTodoByIdUseCase
import com.engineerfred.kotlin.todoapp.feature_todo.domain.use_cases.UpdateTodoUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SaveUpdateTodoViewModel @AssistedInject constructor(
    private val getTodoByIdUseCase: GetTodoByIdUseCase,
    private val addTodoUseCase: AddTodoUseCase,
    private val deleteTodoUseCase: DeleteTodoUseCase,
    private val updateTodoUseCase: UpdateTodoUseCase,
    @Assisted private val todoId: Long
) : ViewModel() {

    var uiState by mutableStateOf(SaveUpdateTodoScreenState())
        private set

    private val todo = MutableStateFlow<Todo?>(null)

    var saveUpdateCompleted by mutableStateOf( false )
        private set

    companion object {
        const val TAG = "TodoApplication"
    }

    init {
        getTask()
    }


    fun onEvent( event: SaveUpdateTodoScreenEvents ) {
        when( event ) {
            SaveUpdateTodoScreenEvents.AchieveClicked -> {
                uiState = uiState.copy( achieved = !uiState.achieved )
                Log.v(TAG, "Archived: ${uiState.achieved}")
            }
            SaveUpdateTodoScreenEvents.CompletedClicked -> {
                uiState = uiState.copy( completed = !uiState.completed )
                Log.v(TAG, "Completed: ${uiState.completed}")
            }
            SaveUpdateTodoScreenEvents.DeleteClicked -> {
                deleteTodo()
            }
            is SaveUpdateTodoScreenEvents.DescriptionChanged -> {
                uiState = uiState.copy(
                    description = event.description,
                )
                uiState = when {
                    uiState.description.isEmpty() -> uiState.copy(descriptionErrorMessage = "A task description is required")
                    uiState.description.length < 6 -> uiState.copy(descriptionErrorMessage = "The provided description is too short")
                    else -> uiState.copy(descriptionErrorMessage = "")
                }
            }
            SaveUpdateTodoScreenEvents.SaveClicked -> {
                if ( isDataValid() ) {
                    if (todoId == -1L) saveTodo() else updateTodo()
                }
            }
            is SaveUpdateTodoScreenEvents.TitleChanged -> {
                uiState = uiState.copy( todoTitle = event.title )
                uiState = when {
                    uiState.todoTitle.isEmpty() -> uiState.copy(titleErrorMessage = "A task title is required")
                    uiState.todoTitle.length < 3 -> uiState.copy(titleErrorMessage = "The provided title is too short")
                    else -> uiState.copy(titleErrorMessage = "")
                }
            }
        }
    }

    private fun saveTodo() {
        viewModelScope.launch{

            val todoEntity = TodoEntity(
                title = uiState.todoTitle,
                description = uiState.description,
                timeStamp = System.currentTimeMillis(),
                completed = uiState.completed,
                archived = uiState.achieved,
                isSynced = uiState.isSynced
            )

            addTodoUseCase.invoke(todoEntity).collect{
                saveUpdateCompleted = when (it) {
                    is Resource.Success -> true
                    else -> false
                }
            }
        }
    }

    private fun updateTodo() {
        val noChangesWereMade = (
                uiState.todo.title == todo.value?.title &&
                        uiState.todo.description == todo.value?.description &&
                        uiState.todo.completed == todo.value?.completed &&
                        uiState.todo.archived == todo.value?.archived
                ) //the user hasn't changed anything!

        if (noChangesWereMade) {
            saveUpdateCompleted = true
            Log.v(TAG, "The user hasn't changed anything!")
        } else {
            Log.v(TAG, "The user changed some fields of the task!")
            viewModelScope.launch {
                updateTodoUseCase.invoke( uiState.todo ).collect{
                    saveUpdateCompleted = when (it) {
                        is Resource.Success -> true
                        else -> false
                    }
                }
            }
        }
    }

    private fun isDataValid() : Boolean {
        return when {
            ( uiState.todo.title.isEmpty() || uiState.todo.title.length < 3 ) -> {
                Log.v(TAG, "The task title is invalid!")
                false
            }
            ( uiState.todo.description.isEmpty() || uiState.todo.description.length < 6 ) -> {
                Log.v(TAG, "The task description is invalid!")
                false
            }
            else -> true
        }
    }

    private fun deleteTodo() {
        if ( todoId != -1L ) {
            viewModelScope.launch{
                todo.value?.let {
                    deleteTodoUseCase.invoke(it).collect{
                        saveUpdateCompleted = when (it) {
                            is Resource.Success -> true
                            else -> false
                        }
                    }
                }
            }
        }
    }

    private fun getTask() {
        Log.v(TAG, "Retrieved todoId is: $todoId")
        if ( todoId != -1L ) {
            viewModelScope.launch {
                getTodoByIdUseCase.invoke(todoId).collect{
                    if ( it is Resource.Success ) {
                        it.result?.let {  retrievedTodo ->
                            todo.value = retrievedTodo  // for when a user decides to delete the task from this screen
                            uiState = uiState.copy(
                                todoId = retrievedTodo.id,
                                todoTitle = retrievedTodo.title,
                                description = retrievedTodo.description,
                                timeStamp = retrievedTodo.timeStamp,
                                achieved = retrievedTodo.archived,
                                isSynced = retrievedTodo.isSynced,
                                completed = retrievedTodo.completed,
                            )
                        }
                    }
                }
                Log.v(TAG, "UiState based on the retrieved task: $uiState")
            }
        } else {
            Log.v(TAG, "Create a new task!")
        }
    }

}