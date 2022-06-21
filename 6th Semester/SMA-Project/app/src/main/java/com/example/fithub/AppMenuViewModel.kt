package com.example.fithub

import android.app.Application
import android.content.res.Resources
import android.graphics.Typeface
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.room.Room

class AppMenuViewModel(application: Application) : AndroidViewModel(application) {

    private val applicationContext = getApplication<Application>().applicationContext


    var dailycal = ""
    var visibility : Boolean =false
    fun getDailyCal(currentUser: String){

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).allowMainThreadQueries().build()


        val userDao=db.userDao()
        val cal= userDao.findLastMeasurement(currentUser!!)
        if(!cal.isEmpty()) {
          dailycal= cal.get(0).calorieReq
            visibility=true

        }else{
            visibility=false
        }


    }

}

