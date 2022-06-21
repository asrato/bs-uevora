package com.example.fithub

data class BMIResponse(
    val status_code: Int,
    val request_result: String,
    val data: BMIData
)