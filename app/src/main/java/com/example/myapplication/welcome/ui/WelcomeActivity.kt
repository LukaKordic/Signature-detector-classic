package com.example.myapplication.welcome.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.example.myapplication.common.*
import com.example.myapplication.welcome.WelcomeContract.WelcomePresenter
import com.example.myapplication.welcome.WelcomeContract.WelcomeView
import kotlinx.android.synthetic.main.activity_welcome.*
import org.koin.android.ext.android.inject
import java.io.FileNotFoundException

class WelcomeActivity : AppCompatActivity(), WelcomeView {
  
  private val presenter: WelcomePresenter by inject()
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_welcome)
    presenter.setView(this)
    initClickActions()
    parseCsv()
  }
  
  override fun checkCameraPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
      // permission is not granted, ask for it
        requestCameraPermission()
      else
        openCamera()
    } else {
      // permission is granted upon installation on versions lower than Marshmallow
      openCamera()
    }
  }
  
  override fun checkReadStoragePermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
      // permission is not granted, ask for it
        requestReadStoragePermission()
      else
        launchGallery()
    } else {
      // permission is granted upon installation on versions lower than Marshmallow
      launchGallery()
    }
  }
  
  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    if (requestCode == RC_CAMERA_PERMISSION && permissions.isNotEmpty()) {
      if (grantResults[0] == PackageManager.PERMISSION_GRANTED) openCamera()
      if (grantResults[1] == PackageManager.PERMISSION_GRANTED) launchGallery()
    }
    
  }
  
  override fun openCamera() {
    Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
      //check if there is component that can handle this intent
      resolveActivity(packageManager)?.also {
        startActivityForResult(this, RC_CAPTURE_IMAGE)
      }
    }
  }
  
  override fun launchGallery() {
    Intent(Intent.ACTION_PICK).apply {
      type = "image/*"
      putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
    }.run {
      resolveActivity(packageManager)?.also {
        startActivityForResult(this, RC_LOAD_IMAGE)
      }
    }
  }
  
  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (resultCode == Activity.RESULT_OK) {
      when (requestCode) {
        RC_CAPTURE_IMAGE -> {
          val image = data?.extras?.get("data") as? Bitmap
          image?.let {
            photoPreview.setImageBitmap(it)
            val image2D = convertImageTo2DArray(it)
            val lbp = LBP(8, 1)
            val lbpResult = lbp.getLBP(image2D)
            val test = lbp.reshape(lbpResult, 0, 148, 0, 148)
            val histArray = lbp.histc(test)
            println()
//        printMatrix(lbpResult)
          }
        }
        RC_LOAD_IMAGE -> {
          val imageUri = data?.data
          imageUri?.let {
            try {
              val inputStream = contentResolver.openInputStream(it)
              val selectedImage = BitmapFactory.decodeStream(inputStream)
              photoPreview.setImageBitmap(selectedImage)
            } catch (ex: FileNotFoundException) {
              ex.printStackTrace()
            }
          }
        }
      }
    }
  }
  
  private fun initClickActions() {
    capturePhoto.setOnClickListener { presenter.capturePhotoClicked() }
    loadFromGallery.setOnClickListener { presenter.loadImageClicked() }
  }
  
  private fun requestCameraPermission() = ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),
                                                                            RC_CAMERA_PERMISSION)
  
  private fun requestReadStoragePermission() =
      ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), RC_STORAGE_PERMISSION)
  
  private fun parseCsv() {
    val inputStream = resources.openRawResource(R.raw.signature_features)
    CSVFile(inputStream).read()
  }

//  private fun loadModel() {
//    val assetManager = assets
//    try {
//      classifier = SerializationHelper.read(assetManager.open("svm_weka.model")) as Classifier
//    } catch (e: IOException) {
//      e.printStackTrace()
//    }
//    println("Model loaded")
//  }
}
