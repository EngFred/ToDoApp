package com.engineerfred.kotlin.todoapp.feature_todo.data.di

import android.content.Context
import androidx.room.Room
import com.engineerfred.kotlin.todoapp.feature_todo.data.local.MIGRATION_1_2
import com.engineerfred.kotlin.todoapp.feature_todo.data.local.MIGRATION_2_3
import com.engineerfred.kotlin.todoapp.feature_todo.data.local.TodoDatabase
import com.engineerfred.kotlin.todoapp.feature_todo.data.local.TodoDatabase.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn( SingletonComponent::class )
object LocalModule {

    @Singleton
    @Provides
    fun provideToDoDatabase( @ApplicationContext context: Context )  =
        Room.databaseBuilder(
            context,
            TodoDatabase::class.java,
            DATABASE_NAME
        ).addMigrations(MIGRATION_1_2, MIGRATION_2_3).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideToDoDao( todoDatabase: TodoDatabase ) =
        todoDatabase.todoDao
}