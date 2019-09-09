package com.example.myapplication.welcome

import android.graphics.Bitmap
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
    fun parseTrainingFeatures(inputStream: InputStream)
    fun parseTrainingLabels(inputStream: InputStream)
  }
}