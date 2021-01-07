package com.foxhole.runningapp.db.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.foxhole.runningapp.model.Run

/**
 * Created by Musfick Jamil on 1/7/2021$.
 */
@Database(
    entities = [Run::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class RunningDatabase : RoomDatabase() {
    abstract fun getRunDao(): RunDao
}