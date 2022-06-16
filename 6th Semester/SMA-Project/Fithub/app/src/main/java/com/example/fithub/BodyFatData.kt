package com.example.fithub

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("Body Fat (BMI method)")
    val body_fat_bmi: Double,
    @SerializedName("Body Fat (U.S. Navy Method)")
    val body_fat_navy: Double,
    @SerializedName("Body Fat Category")
    val body_fat_category: String?,
    @SerializedName("Body Fat Mass")
    val body_fat_mass: Double,
    @SerializedName("Lean Fat Mass")
    val lean_fat_mass: Double
)