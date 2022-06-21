package com.example.fithub

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiInterface {
    @Headers(
        "X-RapidAPI-Host: fitness-calculator.p.rapidapi.com",
        "X-RapidAPI-Key: e08d6c5182msh2b0070c42935064p166f3ajsn6b584bf18d28"
    )
    @GET("bmi")
    fun getBMI(
        @Query("age") age: String,
        @Query("weight") weight: String,
        @Query("height") height: String
    ): Call<BMIResponse>

    @Headers(
        "X-RapidAPI-Host: fitness-calculator.p.rapidapi.com",
        "X-RapidAPI-Key: e08d6c5182msh2b0070c42935064p166f3ajsn6b584bf18d28"
    )
    @GET("bodyfat")
    fun getBodyFat(
        @Query("age") age: String,
        @Query("weight") weight: String,
        @Query("height") height: String,
        @Query("gender") gender: String,
        @Query("neck") neck: String,
        @Query("waist") waist: String,
        @Query("hip") hip: String,
    ): Call<BodyFatResponse>

    @Headers(
        "X-RapidAPI-Host: fitness-calculator.p.rapidapi.com",
        "X-RapidAPI-Key: e08d6c5182msh2b0070c42935064p166f3ajsn6b584bf18d28"
    )
    @GET("dailycalorie")
    fun getDailyCalorie(
        @Query("age") age: String,
        @Query("gender") gender: String,
        @Query("height") height: String,
        @Query("weight") weight: String,
        @Query("activitylevel") activitylevel: String,
    ): Call<CalorieResponse>

    @Headers(
        "X-RapidAPI-Host: fitness-calculator.p.rapidapi.com",
        "X-RapidAPI-Key: e08d6c5182msh2b0070c42935064p166f3ajsn6b584bf18d28"
    )
    @GET("activities")
    fun getActivity(
        @Query("intensitylevel") intensitylevel: String,
    ): Call<ActivityResponse>


}