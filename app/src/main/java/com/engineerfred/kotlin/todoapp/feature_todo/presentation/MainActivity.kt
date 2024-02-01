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
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import com.engineerfred.kotlin.todoapp.feature_todo.data.worker.TasksWorker
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.navigation.AppNavigationGraph
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.theme.ToDoAppTheme
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.view_models.save_update_todo_view_model.SaveUpdateTodoViewModelAssistedFactory
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
            ToDoAppTheme {

                val notificationPermissionRequest = rememberPermissionState(
                    permission = android.Manifest.permission.POST_NOTIFICATIONS
                )

                val workerConstraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED).build()

                LaunchedEffect(Unit) {
                    if ( notificationPermissionRequest.status.isGranted ) {
                        val workRequest = OneTimeWorkRequestBuilder<TasksWorker>()
                            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                            .setBackoffCriteria(
                                backoffPolicy = BackoffPolicy.EXPONENTIAL, //triggered whenever we return a retry from our worker
                                duration = Duration.ofSeconds(15)
                            ).setConstraints(workerConstraints).build()
                        WorkManager.getInstance(applicationContext).enqueue(workRequest)
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



//        val workRequest = PeriodicWorkRequestBuilder<TasksWorker>(
//            repeatInterval = Duration.ofHours(1), // Adjust the interval as needed
//            flexTimeInterval = Duration.ofMinutes(15) // Optional flex time
//        ).build()