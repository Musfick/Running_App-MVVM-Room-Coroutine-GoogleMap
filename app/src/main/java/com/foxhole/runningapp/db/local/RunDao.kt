package com.foxhole.runningapp.db.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.foxhole.runningapp.model.Run

/**
 * Created by Musfick Jamil on 1/7/2021$.
 */
@Dao
interface RunDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRun(run : Run)

    @Delete
    suspend fun deleteRun(run: Run)

    @Query("SELECT * FROM running_table ORDER BY timestamp DESC ")
    fun gelAllRunsBySortedByDate(): LiveData<List<Run>>

    @Query("SELECT * FROM running_table ORDER BY timeInMillis DESC ")
    fun gelAllRunsBySortedByTimeInMillis(): LiveData<List<Run>>

    @Query("SELECT * FROM running_table ORDER BY caloriesBurned DESC ")
    fun gelAllRunsBySortedByCaloriesBurned(): LiveData<List<Run>>

    @Query("SELECT * FROM running_table ORDER BY avgSpeedInKMH DESC ")
    fun gelAllRunsBySortedByAvgSpeed(): LiveData<List<Run>>

    @Query("SELECT * FROM running_table ORDER BY distanceInMeters DESC ")
    fun gelAllRunsBySortedByDistance(): LiveData<List<Run>>

    @Query("SELECT SUM(timeInMillis) FROM running_table")
    fun getTotalTimeInMillis():LiveData<Long>

    @Query("SELECT SUM(caloriesBurned) FROM running_table")
    fun getTotalTimeInCaloriesBurned():LiveData<Int>

    @Query("SELECT SUM(distanceInMeters) FROM running_table")
    fun getTotalTimeInDistance():LiveData<Int>

    @Query("SELECT SUM(avgSpeedInKMH) FROM running_table")
    fun getTotalTimeInAvgSpeed():LiveData<Float>
}