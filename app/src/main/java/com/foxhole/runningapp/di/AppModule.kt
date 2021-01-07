package com.foxhole.runningapp.di

import android.content.Context
import androidx.room.Room
import com.foxhole.runningapp.db.local.RunningDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

/**
 * Created by Musfick Jamil on 1/7/2021$.
 */
@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRunningDatabase(
        @ApplicationContext app : Context
    ) = Room.databaseBuilder(
        app,
        RunningDatabase::class.java,
        "running_app"
    ).build()

    @Singleton
    @Provides
    fun provideRunDao(db : RunningDatabase) = db.getRunDao()
}