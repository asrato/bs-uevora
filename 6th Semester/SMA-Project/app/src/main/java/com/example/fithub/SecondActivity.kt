package com.example.fithub

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.fithub.AlertManager.Companion.showAlertBox
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class SecondActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var analytics: FirebaseAnalytics

    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser != null) {
            toMenu()
        }
    }

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        analytics = Firebase.analytics

        firebaseAuth = FirebaseAuth.getInstance()


        val extras = intent.extras

        val button: Button = findViewById<Button>(R.id.login_btn)
        if (extras != null) {
            val operation = extras.getString("type")
            val textView: TextView = findViewById(R.id.textView)
            val text = if (operation.equals("Register")) {
                getString(R.string.register)
            } else {
                getString(R.string.login)
            }
            textView.text = text
            button.text = text

            if (operation.equals("Login")) {
                val repeat: EditText = findViewById(R.id.confirm_pass_value)
                repeat.isVisible = false
            }
        }

        findViewById<Button>(R.id.back_btn).setOnClickListener {
            oldScreen()
        }

        findViewById<Button>(R.id.login_btn).setOnClickListener {
            val usernameValue = findViewById<EditText>(R.id.username_value).text.toString()
            val passwordValue = findViewById<EditText>(R.id.password_value).text.toString()

            if (button.text.equals(getString(R.string.register))) {
                val confirmPasswordValue =
                    findViewById<EditText>(R.id.confirm_pass_value).text.toString()
                if (usernameValue.isNotEmpty() && passwordValue.isNotEmpty()) {
                    if (!passwordValue.equals(confirmPasswordValue)) {
                        showAlertBox(
                            this,
                            getString(R.string.no_match_pw),
                            neutralButtonText = getString(R.string.try_again)
                        )
                    } else {
                        if (passwordValue.length < 6) {
                            showAlertBox(
                                this,
                                getString(R.string.short_pw),
                                neutralButtonText = getString(R.string.try_again)
                            )
                        } else {
                            firebaseAuth.createUserWithEmailAndPassword(
                                "$usernameValue@fithub.com",
                                passwordValue
                            )
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        oldScreen()
                                    } else {
                                        showAlertBox(
                                            this,
                                            getString(R.string.no_user_register),
                                            neutralButtonText = getString(R.string.try_again)
                                        )
                                    }
                                }
                        }
                    }
                }
            } else {
                if (usernameValue.isNotEmpty() && passwordValue.isNotEmpty()) {
                    firebaseAuth.signInWithEmailAndPassword(
                        "$usernameValue@fithub.com",
                        passwordValue
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val bundle = Bundle()
                            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "1")
                            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "LOGIN")
                            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "ACTION")
                            analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)

                            toMenu()
                        } else {
                            showAlertBox(
                                this,
                                getString(R.string.no_login_user),
                                neutralButtonText = getString(R.string.try_again)
                            )
                        }
                    }
                }
            }
        }
    }

    private fun oldScreen() {
        val intent = Intent(this, MainActivity::class.java)

        startActivity(intent)

    }

    private fun toMenu() {
        val intent = Intent(this, AppMenu::class.java)
        startActivity(intent)
    }
}