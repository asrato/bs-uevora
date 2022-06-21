package com.example.fithub

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "measurements")
data class DailyKCalReq(
    @PrimaryKey val name: String,
    @ColumnInfo(name = "calorie_req") val calorieReq: String
)

