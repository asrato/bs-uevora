package com.example.fithub

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class AlertManager {
    companion object {
        fun showAlertBox(
            context: AppCompatActivity,
            message: String,
            neutralButtonText: String? = null,
            positiveButtonText: String? = null,
            negativeButtonText: String? = null
        ) {
            val alertBuilder = AlertDialog.Builder(context)

            alertBuilder.setMessage(message)
            if (neutralButtonText != null) // neutral button
                alertBuilder.setNeutralButton(neutralButtonText) { _, _ -> }
            if (positiveButtonText != null) // positive button
                alertBuilder.setPositiveButton(positiveButtonText) { _, _ -> }
            if (negativeButtonText != null) // negative button
                alertBuilder.setNegativeButton(negativeButtonText) { _, _ -> }

            val dialogBox: AlertDialog = alertBuilder.create()
            dialogBox.show()
        }
    }
}