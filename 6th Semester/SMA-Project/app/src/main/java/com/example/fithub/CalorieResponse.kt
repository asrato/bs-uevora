package com.example.fithub

data class CalorieResponse(
    val status_code: Int,
    val request_result: String,
    val data: CalorieData
)