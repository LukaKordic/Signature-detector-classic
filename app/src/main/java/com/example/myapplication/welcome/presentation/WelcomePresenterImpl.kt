package com.example.myapplication.welcome.presentation

import android.graphics.Bitmap
import com.example.myapplication.common.*
import com.example.myapplication.welcome.WelcomeContract
import com.example.myapplication.welcome.ml.CustomKNN
import java.io.InputStream

class WelcomePresenterImpl : WelcomeContract.WelcomePresenter {
  private lateinit var view: WelcomeContract.WelcomeView
  private val classifier by lazy { CustomKNN() }
  
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
  
  override fun parseCsvFile(inputStream: InputStream): List<List<Double>> {
    return CSVFile(inputStream).read()
  }
  
  override fun fit(trainingData: Array<DoubleArray>, labels: Array<String>) {
    classifier.fit(trainingData, labels)
  }
  
  override fun classify(features: DoubleArray): SignatureType {
    return when (classifier.predict(features)) {
      ORIGINAL -> SignatureType.ORIGINAL
      FAKE -> SignatureType.FAKE
      else -> SignatureType.ERROR
    }
  }
}