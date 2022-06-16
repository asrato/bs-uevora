package com.example.fithub

import com.google.firebase.firestore.FirebaseFirestore

class FireStoreManager() {
    companion object {
        fun saveData(data: HashMap<String, Any?>, collection: String) {
            val db = FirebaseFirestore.getInstance()
            db.collection(collection)
                .add(data)
                .addOnSuccessListener { _ -> }
                .addOnFailureListener { exception -> println(exception)}
        }
    }
}