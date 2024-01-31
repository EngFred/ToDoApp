package com.engineerfred.kotlin.todoapp.feature_todo.presentation.util

import android.app.AlertDialog
import android.content.Context

fun showDeleteAlertDialog(
    onDeleteClicked: () -> Unit,
    context: Context,
    task: String
) {
    val alertDialog = AlertDialog.Builder(context)
        .setTitle("Delete task: $task?")
        .setMessage("You're about to delete a task! Are you sure of this Action?")
        .setPositiveButton("Delete") { dialog, _ ->
            onDeleteClicked.invoke()
            dialog.dismiss()
        }
        .setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        .create()
    alertDialog.show()
}