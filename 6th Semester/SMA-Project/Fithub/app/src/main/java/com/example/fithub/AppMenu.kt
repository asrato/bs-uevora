package com.example.fithub

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import java.lang.RuntimeException

class AppMenu : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var analytics: FirebaseAnalytics
    private var currentUser: String? =
        FirebaseAuth.getInstance().currentUser?.email?.split("@fithub.com")?.get(0)

    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser == null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_menu)
        analytics = Firebase.analytics
        val viewModel=ViewModelProvider(this).get(AppMenuViewModel::class.java)
        val requirements = findViewById<TextView>(R.id.requirements)

        val textView: TextView = findViewById<TextView>(R.id.username_welcome)
        val helloString = getString(R.string.hello)

        textView.text = "$helloString $currentUser"
        textView.setTypeface(null, Typeface. BOLD)
        val crashButton: Button = findViewById(R.id.crash_option)

        crashButton.isVisible = currentUser.equals("admin")

        findViewById<Button>(R.id.sign_out_btn).setOnClickListener {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "2")
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "LOGOUT")
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "ACTION")
            analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
            logoutFunction()
        }

        findViewById<Button>(R.id.bmi_option).setOnClickListener {
            val intent = Intent(this, BMIActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.receivepic).setOnClickListener {
            val intent = Intent(this, ReceiveProgressPic::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.body_fat_option).setOnClickListener {
            val intent = Intent(this, BodyFatActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.history_option).setOnClickListener {
            val intent = Intent(this, UserHistoryActivity::class.java)
            startActivity(intent)
        }

        crashButton.setOnClickListener {
            throw RuntimeException("Testing app crash")
        }

        findViewById<Button>(R.id.picture_option).setOnClickListener{
            val intent = Intent(this, SendProgressPicActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.calories_option).setOnClickListener{
            val intent = Intent(this, DailyCaloriesActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.suggested_btn).setOnClickListener{
            val intent = Intent(this, SuggestedExerciseActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.about_option).setOnClickListener {
            val intent = Intent(this, AboutComposeAcitivity::class.java)
            startActivity(intent)
        }

        viewModel.getDailyCal(currentUser!!)
        requirements.text= "${getString(R.string.mainMenuCalories)} ${viewModel.dailycal}"
        requirements.isVisible=viewModel.visibility

    }

    private fun logoutFunction() {
        val intent = Intent(this, MainActivity::class.java)
        FirebaseAuth.getInstance().signOut()
        startActivity(intent)
    }
}