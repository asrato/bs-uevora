package com.example.fithub

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.util.*


private const val BASE_URL = "https://fitness-calculator.p.rapidapi.com/"


class SuggestedExerciseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suggested_exercise)
        val backBtn = findViewById<Button>(R.id.suggestion_back_btn)
        visibility(false)
        backBtn.setOnClickListener {
            val intent = Intent(this, AppMenu::class.java)
            startActivity(intent)
        }
        getActivityData()
    }


    private fun visibility(textVisibility: Boolean) {
        findViewById<TextView>(R.id.textView3).isVisible = textVisibility
        findViewById<TextView>(R.id.forexample).isVisible = textVisibility
        findViewById<ProgressBar>(R.id.progressBar).isVisible = !textVisibility
        findViewById<TextView>(R.id.met_value).isVisible = textVisibility
        findViewById<TextView?>(R.id.suggested_exercise).isVisible = textVisibility
        findViewById<TextView>(R.id.examples).isVisible = textVisibility
    }

    private fun translateTest(text: String, view: TextView) {
        val translationConfigs =
            TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.PORTUGUESE)
                .build()

        val translator = Translation.getClient(translationConfigs)

        translator.downloadModelIfNeeded()
            .addOnSuccessListener {
            }
            .addOnFailureListener {
            }
        translator.translate(text)
            .addOnSuccessListener {
                view.text = it
                visibility(true)
            }
            .addOnFailureListener {
            }
    }

    private fun getMetText(met: String): String {
        if (met.toDouble() >= 0.0 && met.toDouble() <= 3.0) {
            return "MET = $met, ${getString(R.string.light)}"
        } else if (met.toDouble() > 3.0 && met.toDouble() <= 6.0) {
            return "MET = $met, ${getString(R.string.moderate)}"
        } else {
            return "MET = $met, ${getString(R.string.vigorous)}"
        }
    }


    private fun getActivityData() {
        val intensity = (2..9).random()
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getActivity(intensity.toString())


        retrofitData.enqueue(object : Callback<ActivityResponse?> {
            @RequiresApi(Build.VERSION_CODES.O)
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<ActivityResponse?>,
                response: Response<ActivityResponse?>
            ) {
                val responseBody = response.body()
                if (responseBody != null) {
                    val activityData = responseBody.data
                    val exercise = activityData.random()
                    val description: TextView = findViewById(R.id.suggested_exercise)
                    val activity: TextView = findViewById(R.id.examples)
                    if (Locale.getDefault().language == "pt") {
                        translateTest(exercise.activity, description)
                        translateTest(exercise.description, activity)
                    } else {
                        description.text = exercise.activity
                        activity.text = exercise.description
                    }
                    val met: TextView = findViewById(R.id.met_value)
                    met.text = "${getMetText(exercise.metValue)}"

                } else {
                    AlertManager.showAlertBox(
                        this@SuggestedExerciseActivity,
                        getString(R.string.request_error),
                        neutralButtonText = getString(R.string.close)
                    )
                }
            }

            override fun onFailure(call: Call<ActivityResponse?>, t: Throwable) {
                AlertManager.showAlertBox(
                    this@SuggestedExerciseActivity,
                    getString(R.string.request_error),
                    neutralButtonText = getString(R.string.close)
                )
            }
        })


    }
}

