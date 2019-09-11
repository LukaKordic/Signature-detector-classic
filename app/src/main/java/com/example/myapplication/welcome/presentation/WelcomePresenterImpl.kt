package com.example.myapplication.welcome.presentation

import android.util.Log
import androidx.annotation.RawRes
import com.example.myapplication.BuildConfig
import com.example.myapplication.common.FAKE
import com.example.myapplication.common.Resources
import com.example.myapplication.welcome.WelcomeContract
import com.example.myapplication.welcome.ml.CustomKNN
import com.example.myapplication.welcome.ml.LBP
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WelcomePresenterImpl(private val resources: Resources) : WelcomeContract.WelcomePresenter {
  private lateinit var view: WelcomeContract.WelcomeView
  private lateinit var trainingFeatures: List<List<Int>>
  private lateinit var labels: List<String>
  private val classifier by lazy { CustomKNN() }
  
  override fun setView(view: WelcomeContract.WelcomeView) {
    this.view = view
  }
  
  override fun loadData(@RawRes signatureFeatures: Int, @RawRes signatureLabels: Int) {
    GlobalScope.launch {
      trainingFeatures = resources.getRawSignatureFeatures(signatureFeatures)
      labels = resources.getRawSignatureLabels(signatureLabels)
      fit()
    }
  }
  
  override fun capturePhotoClicked() = view.checkCameraPermission()
  
  override fun loadImageClicked() = view.checkReadStoragePermission()
  
  override fun recognizeClicked(image: Array<DoubleArray>) {
    GlobalScope.launch(Dispatchers.IO) {
      val lbpHist = getLbpHistogram(image)
      classify(lbpHist.toList()).also {
        if (it == FAKE) view.showResult(it) else view.showContent()
      }
    }
  }
  
  private fun fit() {
    classifier.fit(trainingFeatures, labels)
  }
  
  private fun classify(features: List<Double>): String {
    val result = classifier.predict(features)
    if (BuildConfig.DEBUG) Log.d(this::class.java.simpleName, result)
    return result
  }
  
  private fun getLbpHistogram(image: Array<DoubleArray>): DoubleArray {
    val lbp = LBP(8, 1)
    val lbpResult = lbp.getLBP(image)
//      val lbp1d = lbp.reshape(lbpResult, 0, 148, 0, 148)
//      val cutPoints = doubleArrayOf(0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0)
//      val histogram = lbp.histc(lbp1d, cutPoints)
    val histogram = lbp.getLbpHist(lbpResult)
//    printMatrix(lbpResult)
//    histogram.forEachIndexed { index, it -> println("histogram[$index]: $it ") }
    return histogram
  }
}