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
import com.engineerfred.kotlin.todoapp.feature_todo.data.worker.TasksWorker
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class TodoApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: TasksWorkerFactory
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setWorkerFactory(workerFactory)
            .build()
}

class TasksWorkerFactory @Inject constructor(
    private val cache: TasksDatabase,
    private val service: TasksService
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker {
        return TasksWorker(
            appContext,
            workerParameters,
            cache, service
        )
    }

}