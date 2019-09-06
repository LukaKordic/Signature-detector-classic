package com.example.myapplication.welcome.presentation

import com.example.myapplication.welcome.WelcomeContract

class WelcomePresenterImpl : WelcomeContract.WelcomePresenter {
  private lateinit var view: WelcomeContract.WelcomeView
  
  override fun setView(view: WelcomeContract.WelcomeView) {
    this.view = view
  }
  
  override fun capturePhotoClicked() = view.checkCameraPermission()
  
  override fun loadImageClicked() = view.checkReadStoragePermission()
}