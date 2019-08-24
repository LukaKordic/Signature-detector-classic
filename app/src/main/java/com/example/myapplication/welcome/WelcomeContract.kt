package com.example.myapplication.welcome

interface WelcomeContract {
  
  interface WelcomePresenter {
    fun setView(view: WelcomeView)
    fun capturePhotoClicked()
  }
  
  interface WelcomeView {
    fun openCamera()
    fun checkCameraPermission()
  }
}