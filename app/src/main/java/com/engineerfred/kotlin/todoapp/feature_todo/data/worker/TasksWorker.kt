package com.engineerfred.kotlin.todoapp.feature_todo.data.worker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.engineerfred.kotlin.todoapp.R
import com.engineerfred.kotlin.todoapp.feature_todo.data.local.cache.TasksDatabase
import com.engineerfred.kotlin.todoapp.feature_todo.data.mappers.toTodoDto
import com.engineerfred.kotlin.todoapp.feature_todo.data.remote.TasksService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.net.ConnectException
import java.net.UnknownHostException

@HiltWorker
class TasksWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    @Assisted private val cache: TasksDatabase,
    @Assisted private val service: TasksService
) : CoroutineWorker ( context, workerParams ) {

    companion object {
        const val TAG = "TasksWorker"
    }

    override suspend fun doWork(): Result {
        Log.wtf(TAG, "Worker started!...")

        try {
            setForeground(getForeGroundInfo(applicationContext)) //to show the notification on android 12 and above
            //step 1
            val tasksAddedWhenOffline = cache.savedToPostTodoDao.getAllTodos()
            if ( tasksAddedWhenOffline.isNotEmpty() ) {
                tasksAddedWhenOffline.forEachIndexed { index, task ->
                    Log.wtf(TAG, "Adding task ${index + 1} to the service!")
                    val url = "todos/${task.id}.json"
                    service.addTodo(url, task.toTodoDto().copy(isSynced = true))
                    cache.savedToPostTodoDao.deleteTodo(task)
                    Log.wtf(TAG, "Task ${index + 1} added successfully to the service!")
                }
            }
            //step 2
            val tasksDeletedWhileOffline = cache.deletedTodoDao.getAllDeletedTodos()
            if ( tasksDeletedWhileOffline.isNotEmpty() ) {
                tasksDeletedWhileOffline.forEachIndexed { index, task ->
                    Log.wtf(TAG, "Deleting task ${index + 1} from the service!")
                    service.deleteTodo(task.id)
                    cache.deletedTodoDao.deleteTodo(task)
                    Log.wtf(TAG, "Task ${index + 1} deleted successfully from the service!")
                }
            }

            //step 2
            val tasksUpdateWhenOffline = cache.todoDao.getUnsyncedTodos()
            if ( tasksUpdateWhenOffline.isNotEmpty() ) {
                tasksUpdateWhenOffline.forEachIndexed { index, task ->
                    Log.wtf(TAG, "Updating task ${index + 1} in the service!")
                    service.updateTodo( task.id, task.toTodoDto().copy( isSynced = true ) )
                    cache.todoDao.addUpdateTodo( task.copy( isSynced = true ) )
                    Log.wtf(TAG, "Task ${index + 1} updated successfully in the service!")
                }
            }
            Log.wtf(TAG, "EVERYTHING IS DONE!")
            return Result.success()

        } catch ( e: Exception ) {
            if ( e is ConnectException || e is UnknownHostException ) {
                Log.wtf(TAG, "Retrying...")
                return Result.retry()
            }
            Log.wtf(TAG, "Something didn't go right $e!")
            return Result.failure( Data.Builder().putString("error", e.toString()).build() )
        }
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return getForeGroundInfo(applicationContext)
    }

}

private fun getForeGroundInfo( context: Context ) : ForegroundInfo {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        ForegroundInfo(
            2000,
            createNotification(context),
            ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
        )
    } else {
        ForegroundInfo(
            2000,
            createNotification(context)
        )
    }
}

private fun createNotification(context: Context): Notification {
    val channelId = "main_channel_id"
    val channelName = "Main Channel"

    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.to_do_icon)
        .setContentTitle("TodoApp")
        .setContentText("Syncing tasks...")
        .setOngoing(true)
        .setAutoCancel(true)

    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }
    return builder.build()
}