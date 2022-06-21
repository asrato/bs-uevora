package com.example.fithub

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.room.Room

class DailyCaloriesActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val applicationContext = getApplication<Application>().applicationContext



fun insertDailyCal(caloricNeeds: String, currentUser: String) {

    val db = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java, "database-name"
    ).allowMainThreadQueries().build()


    val userDao = db.userDao()
    userDao.deleteMeasurement(currentUser!!)
    userDao.insertMeasurement(DailyKCalReq(currentUser!!, caloricNeeds))


}

}


