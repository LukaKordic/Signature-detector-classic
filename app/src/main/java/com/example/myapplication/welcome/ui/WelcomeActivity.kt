package com.example.myapplication.welcome.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.example.myapplication.welcome.WelcomeContract.WelcomePresenter
import com.example.myapplication.welcome.WelcomeContract.WelcomeView
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class WelcomeActivity : AppCompatActivity(), WelcomeView {
  
  private val presenter: WelcomePresenter by inject()
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    presenter.setView(this)
    capturePhoto.setOnClickListener { presenter.capturePhotoClicked() }
  }
  
  override fun checkCameraPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
      // permission is not granted, ask for it
        requestCameraPermission()
      else
        openCamera()
    } else {
      // permission is granted upon installation on version lower than Marshmallow
      openCamera()
    }
  }
  
  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    if (requestCode == RC_CAMERA_PERMISSION && permissions.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
      openCamera()
  }
  
  override fun openCamera() {
    startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), RC_CAPTURE_IMAGE)
  }
  
  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == RC_CAPTURE_IMAGE && resultCode == Activity.RESULT_OK)
    //TODO  load image into image view
      println("Image received")
  }
  
  private fun requestCameraPermission() {
    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), RC_CAMERA_PERMISSION)
  }
}

private const val RC_CAPTURE_IMAGE = 100
private const val RC_CAMERA_PERMISSION = 200
