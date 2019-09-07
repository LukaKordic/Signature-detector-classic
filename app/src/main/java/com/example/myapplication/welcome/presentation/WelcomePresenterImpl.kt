package com.example.myapplication.welcome.presentation

import android.graphics.Bitmap
import com.example.myapplication.common.LBP
import com.example.myapplication.common.convertImageTo2DArray
import com.example.myapplication.welcome.WelcomeContract

class WelcomePresenterImpl : WelcomeContract.WelcomePresenter {
  private lateinit var view: WelcomeContract.WelcomeView
  
  override fun setView(view: WelcomeContract.WelcomeView) {
    this.view = view
  }
  
  override fun capturePhotoClicked() = view.checkCameraPermission()
  
  override fun loadImageClicked() = view.checkReadStoragePermission()
  
  override fun recognizeClicked(image: Bitmap) {
    val image2D = convertImageTo2DArray(image)
    val lbp = LBP(8, 1)
    val lbpResult = lbp.getLBP(image2D)
    val test = lbp.reshape(lbpResult, 0, 148, 0, 148)
    val histArray = lbp.histc(test)
//    printMatrix(lbpResult)
  }
}