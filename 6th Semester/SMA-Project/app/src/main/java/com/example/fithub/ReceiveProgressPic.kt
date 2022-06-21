package com.example.fithub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference

class ReceiveProgressPic : AppCompatActivity() {

    private var currentUser: String? =
        FirebaseAuth.getInstance().currentUser?.email?.split("@fithub.com")?.get(0)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receive_progress_pic)

        val backbutton: Button = findViewById(R.id.back_btn_recv)

        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child("$currentUser")
        val imageList: ArrayList<Item> = ArrayList()
        val pBar: ProgressBar = findViewById(R.id.pBar)
        val rView: RecyclerView = findViewById(R.id.rView)
        pBar.visibility = View.VISIBLE

        backbutton.setOnClickListener {
            val intent = Intent(this, AppMenu::class.java)
            startActivity(intent)
        }

        val listAllTask: Task<ListResult> = storageRef.listAll()
        listAllTask.addOnCompleteListener { result ->
            val items: List<StorageReference> = result.result!!.items

            items.forEachIndexed { index, item ->
                item.downloadUrl.addOnSuccessListener {
                    Log.d("item", "$it")
                    imageList.add(Item(it.toString()))
                }.addOnCompleteListener{
                    ImageAdapter(imageList,this).also { rView.adapter = it }
                    rView.layoutManager = LinearLayoutManager(this)
                    pBar.visibility = View.GONE
                }
        }
        }
    }
}