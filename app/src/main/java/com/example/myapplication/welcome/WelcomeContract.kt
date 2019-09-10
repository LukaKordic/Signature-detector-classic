package com.example.myapplication.welcome

import androidx.annotation.RawRes

interface WelcomeContract {
  
  interface WelcomeView {
    fun openCamera()
    fun checkCameraPermission()
    fun checkReadStoragePermission()
    fun launchGallery()
    fun showResult(result: String)
    fun showContent()
  }
  
  interface WelcomePresenter {
    fun setView(view: WelcomeView)
    fun capturePhotoClicked()
    fun loadImageClicked()
    fun recognizeClicked(image: Array<DoubleArray>)
    fun loadData(@RawRes signatureFeatures: Int, @RawRes signatureLabels: Int)
  }
}