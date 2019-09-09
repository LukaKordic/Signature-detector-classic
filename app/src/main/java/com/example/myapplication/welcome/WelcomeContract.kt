package com.example.myapplication.welcome

import android.graphics.Bitmap
import com.example.myapplication.common.SignatureType
import java.io.InputStream

interface WelcomeContract {
  
  interface WelcomeView {
    fun openCamera()
    fun checkCameraPermission()
    fun checkReadStoragePermission()
    fun launchGallery()
  }
  
  interface WelcomePresenter {
    fun setView(view: WelcomeView)
    fun capturePhotoClicked()
    fun loadImageClicked()
    fun recognizeClicked(image: Bitmap)
    fun parseCsvFile(inputStream: InputStream): List<List<Double>>
    fun fit(trainingData: Array<DoubleArray>, labels: Array<String>)
    fun classify(features: DoubleArray): SignatureType
  }
}