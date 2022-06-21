package com.example.fithub

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


private const val BASE_URL = "https://fitness-calculator.p.rapidapi.com/"

private var currentUser: String? =
    FirebaseAuth.getInstance().currentUser?.email?.split("@fithub.com")?.get(0)
class DailyCaloriesActivity : AppCompatActivity() {

    lateinit var timer_sb:SeekBar
    lateinit var timer_tv: TextView
    lateinit var timer_sb2:SeekBar
    lateinit var timer_tv2: TextView
    var activity_level: Int = 1
    var objective: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_daily_calories)
        timer_sb = findViewById(R.id.seekBar) as SeekBar
        timer_tv = findViewById(R.id.timer_tv) as TextView
        timer_tv.text=getString(R.string.level_1)



    val backBtn= findViewById<Button>(R.id.calorie_back_btn)
        backBtn.setOnClickListener{
            val intent = Intent(this, AppMenu::class.java)
            startActivity(intent)
        }

        timer_sb.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, p1: Int, p2: Boolean) {
                    chooseText(p1,timer_tv)
                    activity_level=p1

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

            })

        timer_sb2 = findViewById(R.id.seekBar2) as SeekBar
        timer_tv2 = findViewById(R.id.timer_tv2) as TextView
        timer_tv2.text=getString(R.string.extreme_loss)

        timer_sb2.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, p1: Int, p2: Boolean) {
                    chooseText2(p1,timer_tv2)
                    objective=p1

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        findViewById<Button>(R.id.calculate_calorie_btn).setOnClickListener {
            val age = findViewById<EditText>(R.id.calorie_age).text.toString()
            val weight = findViewById<EditText>(R.id.calorie_weight).text.toString()
            val height = findViewById<EditText>(R.id.calorie_height).text.toString()

            val gender = try {
                findViewById<RadioButton>(findViewById<RadioGroup>(R.id.calorie_gender_group).checkedRadioButtonId).text.toString()
            } catch (e: Exception) {
                ""
            }

            if (age.equals("")) {
                AlertManager.showAlertBox(
                    this,
                    "${getString(R.string.age)} ${getString(R.string.empty_parameter)}",
                    neutralButtonText = getString(R.string.try_again)
                )
            } else if (weight.equals("")) {
                AlertManager.showAlertBox(
                    this,
                    "${getString(R.string.weight)} ${getString(R.string.empty_parameter)}",
                    neutralButtonText = getString(R.string.try_again)
                )
            } else if (height.equals("")) {
                AlertManager.showAlertBox(
                    this,
                    "${getString(R.string.height)} ${getString(R.string.empty_parameter)}",
                    neutralButtonText = getString(R.string.try_again)
                )
            }  else if (gender.equals("")) {
                AlertManager.showAlertBox(
                    this,
                    "${getString(R.string.gender)} ${getString(R.string.empty_parameter)}",
                    neutralButtonText = getString(R.string.try_again)
                )
            } else {
                val level = activity_level.toString()
                getCalorieData(age, weight, height, decodeGender(gender), "level_$level")
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


    private fun getCalorieData(age: String, weight: String, height: String, gender:String, activity_level:String) {
        val viewModel= ViewModelProvider(this).get(DailyCaloriesActivityViewModel::class.java)
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getDailyCalorie(age,gender,height,weight,activity_level)


        retrofitData.enqueue(object : Callback<CalorieResponse?> {
            @RequiresApi(Build.VERSION_CODES.O)
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<CalorieResponse?>, response: Response<CalorieResponse?>) {
                val responseBody = response.body()

                if (responseBody != null) {
                    val CalorieData = responseBody.data

                    val text: TextView = findViewById(R.id.calories_result)

                    val caloricNeeds = chooseProperCalories(CalorieData, objective)
                    text.text =
                        "${getString(R.string.dailyCaloriesResult)} ${caloricNeeds}"



                    viewModel.insertDailyCal(caloricNeeds, currentUser!!)


                } else {
                    AlertManager.showAlertBox(
                        this@DailyCaloriesActivity,
                        getString(R.string.request_error),
                        neutralButtonText = getString(R.string.close)
                    )
                }
            }

            override fun onFailure(call: Call<CalorieResponse?>, t: Throwable) {
                AlertManager.showAlertBox(
                    this@DailyCaloriesActivity,
                    getString(R.string.request_error),
                    neutralButtonText = getString(R.string.close)
                )
            }
        })
    }
    private fun chooseText (x:Int, textView: TextView) {
        when(x){
            1 -> textView.text = getString(R.string.level_1)
            2 -> textView.text =getString(R.string.level_2)
            3 -> textView.text =getString(R.string.level_3)
            4 -> textView.text =getString(R.string.level_4)
            5 -> textView.text =getString(R.string.level_5)
            6 -> textView.text =getString(R.string.level_6)
            else -> textView.text = "undifined"
        }
    }

    private fun chooseText2(x:Int, textView: TextView) {
        when(x){
            1-> textView.text = getString(R.string.extreme_loss)
            2-> textView.text = getString(R.string.loss)
            3-> textView.text = getString(R.string.mild_loss)
            4-> textView.text = getString(R.string.maintain)
            5-> textView.text = getString(R.string.mild_gain)
            6-> textView.text = getString(R.string.gain)
            7-> textView.text = getString(R.string.extreme_gain)
            else-> textView.text = "undifined"
        }
    }

    private fun chooseProperCalories(data: CalorieData, option: Int):String {
        when(option){
            1-> return data.goals.extremeLoss.calory
            2-> return data.goals.loss.calory
            3-> return data.goals.mildLoss.calory
            4-> return data.goals.maintain
            5-> return data.goals.mildGain.calory
            6-> return data.goals.gain.calory
            7-> return data.goals.extremeGain.calory
            else-> return "undifined"
        }
    }

}