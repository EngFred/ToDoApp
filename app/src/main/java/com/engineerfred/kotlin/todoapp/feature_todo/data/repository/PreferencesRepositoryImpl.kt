package com.engineerfred.kotlin.todoapp.feature_todo.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.engineerfred.kotlin.todoapp.feature_todo.data.di.IoDispatcher
import com.engineerfred.kotlin.todoapp.feature_todo.domain.models.TasksOrderPreferences
import com.engineerfred.kotlin.todoapp.feature_todo.domain.repository.PreferencesRepository
import com.engineerfred.kotlin.todoapp.feature_todo.domain.util.DataConstants.SHOW_ARCHIVED_KEY
import com.engineerfred.kotlin.todoapp.feature_todo.domain.util.DataConstants.SORT_DIRECTION_KEY
import com.engineerfred.kotlin.todoapp.feature_todo.domain.util.DataConstants.SORT_TYPE_KEY
import com.engineerfred.kotlin.todoapp.feature_todo.domain.util.DataConstants.THEME_MODE_KEY
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
)  : PreferencesRepository {

    companion object {
        const val TAG = "PreferencesRepository"
    }

    override suspend fun saveTasksOrdering(tasksOrderPreferences: TasksOrderPreferences) {
        dataStore.edit {
            it[SORT_TYPE_KEY] = tasksOrderPreferences.sortingType
            it[SORT_DIRECTION_KEY] = tasksOrderPreferences.sortingDirection
            it[SHOW_ARCHIVED_KEY] = tasksOrderPreferences.showAchieved
        }
    }

    override fun getTasksOrdering(): Flow<TasksOrderPreferences> {
        return dataStore.data.map {
            TasksOrderPreferences(
                sortingType = it[SORT_TYPE_KEY] ?: "time",
                sortingDirection = it[SORT_DIRECTION_KEY] ?: "a_z",
                showAchieved = it[SHOW_ARCHIVED_KEY] ?: false
            )
        }.distinctUntilChanged()
            .flowOn(ioDispatcher)
            .catch {
                Log.v(TAG, "$it")
        }
    }

    override suspend fun saveTheme(isLightTheme: Boolean) {
        dataStore.edit {
            it[THEME_MODE_KEY] = isLightTheme
        }
    }

    override fun getTheme(): Flow<Boolean> {
        return dataStore.data.map {
            it[THEME_MODE_KEY] ?: true
        }.distinctUntilChanged()
            .flowOn( ioDispatcher )
            .catch {
                Log.v(TAG, "$it")
            }
    }
}