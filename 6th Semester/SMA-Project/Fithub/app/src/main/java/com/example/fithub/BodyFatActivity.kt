package com.example.fithub

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.*
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

class BodyFatActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser == null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_body_fat)

        findViewById<Button>(R.id.back_btn).setOnClickListener {
            val intent = Intent(this, AppMenu::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.calculate_body_fat_btn).setOnClickListener {
            val age = findViewById<EditText>(R.id.body_fat_age).text.toString()
            val weight = findViewById<EditText>(R.id.body_fat_weight).text.toString()
            val height = findViewById<EditText>(R.id.body_fat_height).text.toString()
            val gender = try {
                findViewById<RadioButton>(findViewById<RadioGroup>(R.id.body_fat_gender_group).checkedRadioButtonId).text.toString()
            } catch (e: Exception) {
                ""
            }
            val neck = findViewById<EditText>(R.id.body_fat_neck).text.toString()
            val waist = findViewById<EditText>(R.id.body_fat_waist).text.toString()
            val hip = findViewById<EditText>(R.id.body_fat_hip).text.toString()

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
            } else if (gender.equals("")) {
                showAlertBox(
                    this,
                    "${getString(R.string.gender)} ${getString(R.string.empty_parameter)}",
                    neutralButtonText = getString(R.string.try_again)
                )
            } else if (neck.equals("")) {
                showAlertBox(
                    this,
                    "${getString(R.string.neck)} ${getString(R.string.empty_parameter)}",
                    neutralButtonText = getString(R.string.try_again)
                )
            } else if (waist.equals("")) {
                showAlertBox(
                    this,
                    "${getString(R.string.waist)} ${getString(R.string.empty_parameter)}",
                    neutralButtonText = getString(R.string.try_again)
                )
            } else if (hip.equals("")) {
                showAlertBox(
                    this,
                    "${getString(R.string.hip)} ${getString(R.string.empty_parameter)}",
                    neutralButtonText = getString(R.string.try_again)
                )
            } else {
                getBodyFatData(age, weight, height, decodeGender(gender), neck, waist, hip)
            }
        }
    }

    private fun decodeGender(gender: String): String {
        return if (gender.equals("Homem") || gender.equals("Male"))
            "male"
        else if (gender.equals("Mulher") || gender.equals("Female"))
            "female"
        else ""
    }

    private fun getBodyFatData(
        age: String, weight: String, height: String, gender: String, neck: String,
        waist: String, hip: String
    ) {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getBodyFat(age, weight, height, gender, neck, waist, hip)

        val user = FirebaseAuth.getInstance().currentUser!!.email

        retrofitData.enqueue(object : Callback<BodyFatResponse?> {
            @RequiresApi(Build.VERSION_CODES.O)
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<BodyFatResponse?>,
                response: Response<BodyFatResponse?>
            ) {
                val responseBody = response.body()
                if (responseBody != null) {
                    val bodyFatData = responseBody.data

                    val text: TextView = findViewById(R.id.body_fat_value)
                    if (bodyFatData.body_fat_category != null)
                        text.text =
                            "${bodyFatData.body_fat_bmi}% -> ${bodyFatData.body_fat_category}"
                    else
                        text.text = "${bodyFatData.body_fat_bmi}%"

                    val bodyFatHash = hashMapOf(
                        "type" to "body_fat",
                        "user" to user,
                        "date" to LocalDateTime.now(),
                        "age" to age,
                        "weight" to weight,
                        "height" to height,
                        "gender" to gender,
                        "neck" to neck,
                        "waist" to waist,
                        "hip" to hip,
                        "body_fat_bmi" to bodyFatData.body_fat_bmi,
                        "body_fat_category" to bodyFatData.body_fat_category
                    )

                    val db = FirebaseFirestore.getInstance()
                    db.collection("users_history").add(bodyFatHash)
                } else {
                    showAlertBox(
                        this@BodyFatActivity,
                        getString(R.string.request_error),
                        neutralButtonText = getString(R.string.close)
                    )
                }
            }

            override fun onFailure(call: Call<BodyFatResponse?>, t: Throwable) {
                showAlertBox(
                    this@BodyFatActivity,
                    getString(R.string.request_error),
                    neutralButtonText = getString(R.string.close)
                )
            }
        })
    }
}