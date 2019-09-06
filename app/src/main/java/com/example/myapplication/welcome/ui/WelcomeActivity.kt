package com.example.myapplication.welcome.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
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
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import weka.classifiers.Classifier
import weka.core.SerializationHelper
import java.io.IOException

class WelcomeActivity : AppCompatActivity(), WelcomeView {
  
  private val presenter: WelcomePresenter by inject()
  private lateinit var classifier: Classifier
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    presenter.setView(this)
    capturePhoto.setOnClickListener { presenter.capturePhotoClicked() }
    parseCsv()
  }
  
  private fun requestCameraPermission() = ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),
                                                                            RC_CAMERA_PERMISSION)
  
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
  
  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    if (requestCode == RC_CAMERA_PERMISSION && permissions.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
      openCamera()
  }
  
  override fun openCamera() {
    Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
      //check if there is component that can handle this intent
      resolveActivity(packageManager)?.also {
        startActivityForResult(this, RC_CAPTURE_IMAGE)
      }
    }
  }
  
  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == RC_CAPTURE_IMAGE && resultCode == Activity.RESULT_OK) {
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
  }
  
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
