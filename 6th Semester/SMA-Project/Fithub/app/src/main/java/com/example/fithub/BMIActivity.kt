package com.example.fithub

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.fithub.AlertManager.Companion.showAlertBox
import com.example.fithub.FireStoreManager.Companion.saveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime

private const val BASE_URL = "https://fitness-calculator.p.rapidapi.com/"

class BMIActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser == null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getBMIData(age: String, weight: String, height: String) {

        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getBMI(age, weight, height)

        val email = FirebaseAuth.getInstance().currentUser!!.email

        retrofitData.enqueue(object : Callback<BMIResponse?> {
            @RequiresApi(Build.VERSION_CODES.O)
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<BMIResponse?>, response: Response<BMIResponse?>) {
                val responseBody = response.body()
                if (responseBody != null) {
                    val bmiData = responseBody.data

                    val text: TextView = findViewById(R.id.bmi_result)
                    text.text =
                        "[${getString(R.string.bmi_option)}] ${bmiData.bmi} -> ${bmiData.health} (${bmiData.healthy_bmi_range})"

                    val bmiHash = hashMapOf(
                        "type" to "bmi",
                        "user" to email,
                        "date" to LocalDateTime.now(),
                        "age" to age,
                        "weight" to weight,
                        "height" to height,
                        "bmi" to bmiData.bmi,
                        "range" to bmiData.healthy_bmi_range,
                        "health" to bmiData.health
                    )

                    val db = FirebaseFirestore.getInstance()
                    db.collection("users_history").add(bmiHash)

                } else {
                    showAlertBox(
                        this@BMIActivity,
                        getString(R.string.request_error),
                        neutralButtonText = getString(R.string.close)
                    )
                }
            }

            override fun onFailure(call: Call<BMIResponse?>, t: Throwable) {
                showAlertBox(
                    this@BMIActivity,
                    getString(R.string.request_error),
                    neutralButtonText = getString(R.string.close)
                )
            }
        })
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInsanceState: Bundle?) {
        super.onCreate(savedInsanceState)
        setContentView(R.layout.activity_bmi)

        findViewById<Button>(R.id.back_btn).setOnClickListener {
            val intent = Intent(this, AppMenu::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.calculate_bmi_btn).setOnClickListener {
            val age = findViewById<EditText>(R.id.bmi_age).text.toString()
            val weight = findViewById<EditText>(R.id.bmi_weight).text.toString()
            val height = findViewById<EditText>(R.id.bmi_height).text.toString()

            if (age.equals("")) {
                showAlertBox(
                    this,
                    "${getString(R.string.age)} ${getString(R.string.empty_parameter)}",
                    neutralButtonText = getString(R.string.try_again)
                )
            } else if (weight.equals("")) {
                showAlertBox(
                    this,
                    "${getString(R.string.weight)} ${getString(R.string.empty_parameter)}",
                    neutralButtonText = getString(R.string.try_again)
                )
            } else if (height.equals("")) {
                showAlertBox(
                    this,
                    "${getString(R.string.height)} ${getString(R.string.empty_parameter)}",
                    neutralButtonText = getString(R.string.try_again)
                )
            } else {
                getBMIData(age, weight, height)
            }
        }
    }
}