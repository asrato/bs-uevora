package com.example.fithub

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    @Query("SELECT * FROM measurements WHERE name LIKE :username ")
    fun findLastMeasurement(username: String): List<DailyKCalReq>

    @Insert
    fun insertMeasurement(dailyKCalReq: DailyKCalReq)

    @Query("DELETE FROM measurements WHERE name LIKE :username")
    fun deleteMeasurement(username: String)


}