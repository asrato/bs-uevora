package com.example.fithub

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*
import android.Manifest
import android.app.Activity
import android.graphics.Bitmap
import android.os.Environment
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.text.SimpleDateFormat


class SendProgressPicActivity : AppCompatActivity() {

    private val REQUEST_IMAGE_CAPTURE = 100

    private lateinit var photoFile: File
    lateinit var currentPhotoPath:String
  //  private val PICTURE_FROM_CAMERA: Int = 1

    private var currentUser: String? =
        FirebaseAuth.getInstance().currentUser?.email?.split("@fithub.com")?.get(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_progress_pic)

        val backbutton: Button = findViewById(R.id.back_btn_pic)
        val takePicture: Button = findViewById(R.id.openCamera)

        val sendPicture: Button = findViewById(R.id.sendPic)
        sendPicture.isVisible = false

        backbutton.setOnClickListener {
            val intent = Intent(this, AppMenu::class.java)
            startActivity(intent)
        }

        takePicture.setOnClickListener {
            checkPermissionAndOpenCamera()
        }

        sendPicture.setOnClickListener {
            uploadPic()
            val intent = Intent(this, AppMenu::class.java)
            startActivity(intent)
        }
    }

    fun checkPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 5)

        } else {
            takePhotoFromCamera()
        }
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photoFile = createImageFile()
        val uri= FileProvider.getUriForFile(this, "com.example.retrofittest.fileprovider", photoFile)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    private fun createImageFile(): File {
        val timeStamp: String= SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File?=getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply{
            currentPhotoPath = absolutePath}
    }

   override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                var imageView: ImageView = findViewById(R.id.progressImage)
                val uri = FileProvider.getUriForFile(this, "com.example.retrofittest.fileprovider", photoFile)
                imageView.setImageURI(uri)
                val sendPicture: Button = findViewById(R.id.sendPic)
                sendPicture.isVisible = true
            }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun uploadPic() {
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_MM_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        val storageReference = FirebaseStorage.getInstance().getReference("$currentUser/$fileName")
        val uri = FileProvider.getUriForFile(this, "com.example.retrofittest.fileprovider", photoFile)

        storageReference.putFile(uri)
    }
}