package com.example.fithub

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserHistoryActivity : AppCompatActivity() {

    lateinit var customAdapter: CustomAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    private var currentUser: String? =
        FirebaseAuth.getInstance().currentUser?.email

    private var list = ArrayList<UserHistoryData>()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_history)

        val recycleview_data = findViewById<RecyclerView>(R.id.recyclerview_data)

        recycleview_data.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(this)
        recycleview_data.layoutManager = linearLayoutManager

        val db = FirebaseFirestore.getInstance()
        db.collection("users_history")
            .whereEqualTo("user", currentUser)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val data = UserHistoryData(
                        document.id,
                        document.data["age"] as String,
                        document.data["height"] as String,
                        document.data["weight"] as String,
                        document.data["type"] as String
                    )
                    if (data.type.equals("bmi")) {
                        data.bmi = document.data["bmi"] as Number
                        data.category = document.data["health"] as String
                    } else {
                        data.bmi = document.data["body_fat_bmi"] as Number
                        data.category = document.data["body_fat_category"] as String
                    }
                    list.add(data)
                }
                customAdapter = CustomAdapter(baseContext, list)
                customAdapter.notifyDataSetChanged()
                recycleview_data.adapter = customAdapter
            }

        findViewById<Button>(R.id.back_btn).setOnClickListener {
            val intent = Intent(this, AppMenu::class.java)
            startActivity(intent)
        }
    }
}