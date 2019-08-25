package com.example.myapplication.welcome.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.example.myapplication.R
import com.example.myapplication.common.IMG_HEIGHT
import com.example.myapplication.common.IMG_WIDTH
import com.example.myapplication.common.RC_CAMERA_PERMISSION
import com.example.myapplication.common.RC_CAPTURE_IMAGE
import com.example.myapplication.welcome.WelcomeContract.WelcomePresenter
import com.example.myapplication.welcome.WelcomeContract.WelcomeView
import de.lmu.ifi.dbs.jfeaturelib.features.LocalBinaryPatterns
import ij.process.ImageProcessor
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import weka.classifiers.Classifier
import weka.classifiers.pmml.consumer.SupportVectorMachineModel
import weka.classifiers.trees.RandomForest
import weka.core.neighboursearch.NearestNeighbourSearch

class WelcomeActivity : AppCompatActivity(), WelcomeView {
  
  private val presenter: WelcomePresenter by inject()
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    presenter.setView(this)
    capturePhoto.setOnClickListener { presenter.capturePhotoClicked() }
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
        val resizedImage = Bitmap.createScaledBitmap(it, IMG_WIDTH, IMG_HEIGHT, false) //to be used for feature extraction and
        // classification
        photoPreview.setImageBitmap(it)
        val featureExtractor = LocalBinaryPatterns().apply {
          setNeighborhoodSize(8)
          numPoints = 8
          radius = 1.0
          numberOfHistogramBins = 11
        }
      }
    }
  }
}