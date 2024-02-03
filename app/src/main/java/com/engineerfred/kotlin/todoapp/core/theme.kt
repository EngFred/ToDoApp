package com.engineerfred.kotlin.todoapp.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue


@Composable
fun changeTheme( lightTheme: Boolean ) : Boolean {

    var isLightTheme by remember {
        mutableStateOf(lightTheme)
    }

    return isLightTheme
}
