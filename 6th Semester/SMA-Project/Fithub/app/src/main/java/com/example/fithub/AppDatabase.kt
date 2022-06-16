package com.example.fithub

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [DailyKCalReq::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao




}