package com.example.fithub

import com.google.gson.annotations.SerializedName

class CalorieGoals (
    @SerializedName("maintain weight") var maintain:String,
    @SerializedName("Mild weight loss") var mildLoss:CalorieChange,
    @SerializedName("Weight loss") var loss:CalorieChange,
    @SerializedName("Extreme weight loss") var extremeLoss:CalorieChange,
    @SerializedName("Mild weight gain") var mildGain:CalorieChange,
    @SerializedName("Weight gain") var gain:CalorieChange,
    @SerializedName("Extreme weight gain") var extremeGain:CalorieChange,
)
