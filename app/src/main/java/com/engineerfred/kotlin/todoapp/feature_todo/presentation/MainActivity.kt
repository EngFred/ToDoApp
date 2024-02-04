package com.engineerfred.kotlin.todoapp.feature_todo.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.engineerfred.kotlin.todoapp.feature_todo.data.worker.ReminderWorker
import com.engineerfred.kotlin.todoapp.feature_todo.data.worker.TasksSyncWorker
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.navigation.AppNavigationGraph
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.theme.ToDoAppTheme
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.view_models.save_update_todo_view_model.SaveUpdateTodoViewModelAssistedFactory
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.view_models.todos_list_view_model.TodosListViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import dagger.hilt.android.AndroidEntryPoint
import java.time.Duration
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var saveUpdateTodoViewModelAssistedFactory: SaveUpdateTodoViewModelAssistedFactory

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            val todosListViewModel = hiltViewModel<TodosListViewModel>()

            ToDoAppTheme( useDarkTheme = todosListViewModel.uiState.isLightTheme ) {
                val notificationPermissionRequest = rememberPermissionState(
                    permission = android.Manifest.permission.POST_NOTIFICATIONS
                )

                val taskSyncConstraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED).build()

                val taskReminderConstraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.NOT_REQUIRED).build()

                LaunchedEffect(Unit) {
                    if ( notificationPermissionRequest.status.isGranted ) {

                        val workRequest = OneTimeWorkRequestBuilder<TasksSyncWorker>()
                            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                            .setBackoffCriteria(
                                BackoffPolicy.EXPONENTIAL,
                                Duration.ofSeconds(15)
                            ).setConstraints(taskSyncConstraints).build()
                        WorkManager.getInstance(applicationContext).enqueue(workRequest)

//                        val tasksSyncRequest = PeriodicWorkRequestBuilder<TasksSyncWorker>(
//                            repeatInterval = Duration.ofHours(8),
//                            flexTimeInterval = Duration.ofHours(4)
//                        ).setConstraints(taskSyncConstraints).build()
//                        WorkManager.getInstance(applicationContext)
//                            .enqueueUniquePeriodicWork(
//                                "tasks_sync_work",
//                                ExistingPeriodicWorkPolicy.KEEP,
//                                tasksSyncRequest
//                            )


                        val reminderWorkRequest = PeriodicWorkRequestBuilder<ReminderWorker>(
                            repeatInterval = Duration.ofHours(23),
                            flexTimeInterval = Duration.ofHours(12)
                        ).setConstraints(taskReminderConstraints).build()
                        WorkManager.getInstance(applicationContext)
                            .enqueueUniquePeriodicWork(
                                "reminder_notification_work",
                                ExistingPeriodicWorkPolicy.KEEP,
                                reminderWorkRequest
                            )

                    } else {
                        notificationPermissionRequest.launchPermissionRequest()
                    }
                }
                TodoApplication(saveUpdateTodoViewModelAssistedFactory)
            }
        }
    }
}

@Composable
private fun TodoApplication(
    saveUpdateTodoViewModelAssistedFactory: SaveUpdateTodoViewModelAssistedFactory
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        AppNavigationGraph(saveUpdateTodoViewModelAssistedFactory = saveUpdateTodoViewModelAssistedFactory)
    }
}