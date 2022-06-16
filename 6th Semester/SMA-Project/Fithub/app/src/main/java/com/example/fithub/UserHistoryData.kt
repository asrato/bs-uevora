package com.example.fithub

data class UserHistoryData(
    val id: String,
    val age: String,
    val height: String,
    val weight: String,
    val type: String,
    var bmi: Number? = 0,
    var category: String? = ""
)