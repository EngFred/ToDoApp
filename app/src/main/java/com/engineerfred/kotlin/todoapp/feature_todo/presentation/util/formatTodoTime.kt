package com.engineerfred.kotlin.todoapp.feature_todo.presentation.util

import java.text.SimpleDateFormat
import java.util.Locale

fun formatTodoTime( todoTime: Long? ) : String {
    return if ( todoTime != null ) {
        SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH).format(todoTime)
    } else {
        "-"
    }
}