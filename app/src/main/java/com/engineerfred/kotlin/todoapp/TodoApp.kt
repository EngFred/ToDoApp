package com.engineerfred.kotlin.todoapp


import android.app.Application
import android.content.Context
import android.util.Log
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.engineerfred.kotlin.todoapp.feature_todo.data.local.cache.TasksDatabase
import com.engineerfred.kotlin.todoapp.feature_todo.data.remote.TasksService
import com.engineerfred.kotlin.todoapp.feature_todo.data.worker.ReminderWorker
import com.engineerfred.kotlin.todoapp.feature_todo.data.worker.TasksSyncWorker
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class TodoApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: CombinedWorkerFactory


    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setWorkerFactory(workerFactory)
            .build()
    }
}

class CombinedWorkerFactory @Inject constructor(
    private val cache: TasksDatabase,
    private val service: TasksService
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker {
        when (workerClassName) {
            TasksSyncWorker::class.java.name -> return TasksSyncWorker(
                appContext,
                workerParameters,
                cache,
                service
            )
            ReminderWorker::class.java.name -> return ReminderWorker(
                appContext,
                workerParameters,
                cache,
                service
            )
            else -> throw IllegalArgumentException("Unknown worker class: $workerClassName")
        }
    }
}

//class TasksWorkerFactory @Inject constructor(
//    private val cache: TasksDatabase,
//    private val service: TasksService
//) : WorkerFactory() {
//    override fun createWorker(
//        appContext: Context,
//        workerClassName: String,
//        workerParameters: WorkerParameters
//    ): ListenableWorker {
//        return TasksWorker(
//            appContext,
//            workerParameters,
//            cache, service
//        )
//    }
