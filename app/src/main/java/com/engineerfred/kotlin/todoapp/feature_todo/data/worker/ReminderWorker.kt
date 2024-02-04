package com.engineerfred.kotlin.todoapp.feature_todo.data.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.engineerfred.kotlin.todoapp.R
import com.engineerfred.kotlin.todoapp.feature_todo.data.local.cache.TasksDatabase
import com.engineerfred.kotlin.todoapp.feature_todo.data.mappers.toTodoDto
import com.engineerfred.kotlin.todoapp.feature_todo.data.remote.TasksService
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.MainActivity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import retrofit2.HttpException
import java.net.ConnectException
import java.net.UnknownHostException

@HiltWorker
class ReminderWorker @AssistedInject constructor (
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    @Assisted private val cache: TasksDatabase,
    @Assisted private val service: TasksService
) : CoroutineWorker( context, workerParams ) {


    companion object {
        const val TAG = "reminderWorker"
    }
    override suspend fun doWork(): Result {
        try {
            Log.wtf(TAG, "The reminder worker has started!")
            val nearDueDateTasks = cache.todoDao.getAllScheduledTodos()
            if ( nearDueDateTasks.isNotEmpty() ) {
                Log.wtf(TAG, "${nearDueDateTasks.size} ${if ( nearDueDateTasks.size == 1 ) "task is" else "tasks are"} scheduled at the moment!")
                nearDueDateTasks.forEach{ task ->
                    if (task.dueDate!! == System.currentTimeMillis() ) {
                        showNotification( applicationContext,"Your task: ${task.title} needs attention!", task.id.toInt() )
                        cache.todoDao.addUpdateTodo( task.copy( dueDate = null , isSynced = false ) )
                        service.updateTodo(task.id, task.toTodoDto().copy( dueDate = null, isSynced = true ))
                        cache.todoDao.addUpdateTodo( task.copy( isSynced = true ) )
                    } else {
                        Log.wtf(TAG, "Scheduled tasks exist but there time is not yet!")
                    }
                }
                return Result.success()
            } else {
                Log.wtf(TAG, "No tasks are scheduled at the moment!")
                return Result.success()
            }

        }catch ( ex: Exception ) {
            if ( ex is ConnectException || ex is UnknownHostException || ex is HttpException ) {
                Log.wtf(TAG, "Error in ReminderWorker due to connection issues! $ex")
            }
            Log.wtf(TAG, "Error in ReminderWorker! $ex")
            return Result.failure()
        }
    }

//    private fun getForeGroundInfo( content: String, taskId: Int ) : ForegroundInfo {
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            ForegroundInfo(
//                taskId,
//                showNotification(applicationContext, content),
//                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
//            )
//        } else {
//            ForegroundInfo(
//                taskId,
//                showNotification(applicationContext, content)
//            )
//        }
//    }
}


private fun showNotification(
    context: Context,
    content: String,
    taskId: Int,
) {

    //  No back-stack when launched
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val pendingIntent = PendingIntent.getActivity(
        context, 0, intent,
        PendingIntent.FLAG_IMMUTABLE
    )

    val channelId = "main_reminder_channel_id"
    val channelName = "Main Remainder Channel"

    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.to_do_icon)
        .setContentTitle("Task Reminder")
        .setContentText(content)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true) // Remove notification when tapped
        .setVisibility(VISIBILITY_PUBLIC) // Show on lock screen

    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel) // This won't create a new channel everytime, safe to call
    }
    notificationManager.notify(taskId, builder.build())
}
