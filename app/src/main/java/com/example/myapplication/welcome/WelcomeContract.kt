package com.example.myapplication.welcome

import java.io.InputStream

interface WelcomeContract {
  
  interface WelcomeView {
    fun openCamera()
    fun checkCameraPermission()
    fun checkReadStoragePermission()
    fun launchGallery()
    fun showResult(result: String)
  }
  
  interface WelcomePresenter {
    fun setView(view: WelcomeView)
    fun capturePhotoClicked()
    fun loadImageClicked()
    fun recognizeClicked(image: Array<DoubleArray>)
    fun parseTrainingFeatures(inputStream: InputStream)
    fun parseTrainingLabels(inputStream: InputStream)
  }
}