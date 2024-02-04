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
        const val TAG = "SaveUpdateTaskViewModel"
    }

    init {
        getTask()
    }


    fun onEvent( event: SaveUpdateTodoScreenEvents ) {
        when( event ) {
            SaveUpdateTodoScreenEvents.AchieveClicked -> {
                uiState = uiState.copy( prioritized = !uiState.prioritized )
                Log.v(TAG, "Archived: ${uiState.prioritized}")
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

            is SaveUpdateTodoScreenEvents.DueDateSelected -> {
                if ( event.date < System.currentTimeMillis() )
                    uiState = uiState.copy( invalidDateMessage = "Selected date is invalid" )
                else
                    uiState = uiState.copy( dueDate = event.date, invalidDateMessage = "" )
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
                prioritized = uiState.prioritized,
                isSynced = uiState.isSynced,
                dueDate = uiState.dueDate
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
                        uiState.todo.prioritized == todo.value?.prioritized &&
                        uiState.todo.dueDate == todo.value?.dueDate
                ) //the user hasn't changed anything!

        if (noChangesWereMade) {
            saveUpdateCompleted = true
            Log.v(TAG, "The user hasn't changed anything!")
        } else {
            Log.v(TAG, "The user changed some fields of the task!")
            viewModelScope.launch {
                updateTodoUseCase.invoke( uiState.todo )
                saveUpdateCompleted = true
            }
        }
    }

    private fun isDataValid() : Boolean {
        when {
            ( uiState.todo.title.isEmpty() || uiState.todo.title.length < 3 ) -> {
                Log.v(TAG, "The task title is invalid!")
                return false
            }
            ( uiState.dueDate != null ) -> {
                if ( uiState.dueDate!! < System.currentTimeMillis() ) {
                    Log.v(TAG, "The selected date is invalid")
                    return false
                }
                return true
            }
            ( uiState.todo.description.isEmpty() || uiState.todo.description.length < 6 ) -> {
                Log.v(TAG, "The task description is invalid!")
                return false
            }
            else -> return true
        }
    }

    private fun deleteTodo() {
        if ( todoId != -1L ) {
            viewModelScope.launch{
                todo.value?.let {
                    deleteTodoUseCase.invoke(it)
                    saveUpdateCompleted = true
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
                                prioritized = retrievedTodo.prioritized,
                                isSynced = retrievedTodo.isSynced,
                                completed = retrievedTodo.completed,
                                dueDate = retrievedTodo.dueDate
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