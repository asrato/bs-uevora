package com.example.fithub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser != null) {
            val intent = Intent(this, AppMenu::class.java)
            startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.login).setOnClickListener {
            loginScreen()
        }

        findViewById<Button>(R.id.register).setOnClickListener {
            registerScreen()
        }
    }

    private fun registerScreen() {
        val intent = Intent(this, SecondActivity::class.java)
        intent.putExtra("type", "Register")
        startActivity(intent)
    }

    private fun loginScreen() {
        val intent = Intent(this, SecondActivity::class.java)
        intent.putExtra("type", "Login")
        startActivity(intent)
    }
    private fun openNativeCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivity(intent)
    }
}