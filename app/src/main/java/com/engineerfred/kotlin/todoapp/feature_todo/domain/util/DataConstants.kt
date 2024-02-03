package com.engineerfred.kotlin.todoapp.feature_todo.domain.util

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object DataConstants {
    val THEME_MODE_KEY = booleanPreferencesKey("THEME_MODE_KEY")
    val SORT_TYPE_KEY = stringPreferencesKey("SORT_TYPE_KEY")
    val SORT_DIRECTION_KEY = stringPreferencesKey("SORT_DIRECTION_KEY")
    val SHOW_ARCHIVED_KEY = booleanPreferencesKey("SHOW_ARCHIVED_KEY ")
}