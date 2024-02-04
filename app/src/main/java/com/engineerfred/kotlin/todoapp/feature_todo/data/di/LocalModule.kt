package com.engineerfred.kotlin.todoapp.feature_todo.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.engineerfred.kotlin.todoapp.feature_todo.data.local.cache.MIGRATION_2_1
import com.engineerfred.kotlin.todoapp.feature_todo.data.local.cache.TasksDatabase
import com.engineerfred.kotlin.todoapp.feature_todo.data.local.cache.TasksDatabase.Companion.DATABASE_NAME
import com.engineerfred.kotlin.todoapp.feature_todo.data.repository.PreferencesRepositoryImpl
import com.engineerfred.kotlin.todoapp.feature_todo.domain.repository.PreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn( SingletonComponent::class )
object LocalModule {

    @Singleton
    @Provides
    fun provideToDoDatabase( @ApplicationContext context: Context )  =
        Room.databaseBuilder(
            context,
            TasksDatabase::class.java,
            DATABASE_NAME
        ).addMigrations(MIGRATION_2_1).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideToDoDao(tasksDatabase: TasksDatabase) =
        tasksDatabase.todoDao

    @Singleton
    @Provides
    fun provideDatastoreInstance(  @ApplicationContext context : Context  ) = PreferenceDataStoreFactory.create {
        context.preferencesDataStoreFile("settings")
    }

    @Singleton
    @Provides
    fun provideThemeRepository(
        dataStore: DataStore<Preferences>,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ) : PreferencesRepository = PreferencesRepositoryImpl( dataStore, ioDispatcher )

}