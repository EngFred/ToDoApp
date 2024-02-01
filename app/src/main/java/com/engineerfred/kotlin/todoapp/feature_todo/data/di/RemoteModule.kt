package com.engineerfred.kotlin.todoapp.feature_todo.data.di

import com.engineerfred.kotlin.todoapp.core.util.Constants.BASE_URL
import com.engineerfred.kotlin.todoapp.feature_todo.data.local.TasksDatabase
import com.engineerfred.kotlin.todoapp.feature_todo.data.remote.TasksService
import com.engineerfred.kotlin.todoapp.feature_todo.data.repository.TasksRepositoryImpl
import com.engineerfred.kotlin.todoapp.feature_todo.domain.repository.TasksRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn( SingletonComponent::class )
object RemoteModule {

    @Provides
    @Singleton
    fun provideRetrofitInstance() : TasksService {
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        val httpClient = OkHttpClient().newBuilder().addInterceptor(httpLoggingInterceptor)
        httpClient.readTimeout(60, TimeUnit.SECONDS).build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
            .create(TasksService::class.java)
    }

    @Provides
    @Singleton
    fun provideTodosRepository(service: TasksService, cache: TasksDatabase, @IoDispatcher ioDispatcher: CoroutineDispatcher ) : TasksRepository {
        return  TasksRepositoryImpl(  service, cache, ioDispatcher )
    }

}

//In networking, "read timeout" specifically refers to the maximum time that the client will wait for the server to send data after the connection has been established. If the server takes longer than the specified timeout to send the response, the client considers it a timeout and may take appropriate action.