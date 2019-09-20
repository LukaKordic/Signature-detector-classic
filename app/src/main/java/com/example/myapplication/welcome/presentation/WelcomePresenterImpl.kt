package com.example.myapplication.welcome.presentation

import android.util.Log
import androidx.annotation.RawRes
import com.example.myapplication.BuildConfig
import com.example.myapplication.common.constants.FAKE
import com.example.myapplication.common.utils.IMG_HEIGHT
import com.example.myapplication.common.utils.IMG_WIDTH
import com.example.myapplication.common.utils.Resources
import com.example.myapplication.welcome.WelcomeContract
import com.example.myapplication.welcome.ml.CustomKNN
import com.example.myapplication.welcome.ml.LBP
import com.example.myapplication.welcome.ml.Lbp
import com.example.myapplication.welcome.ml.Lbp.printMatrix
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WelcomePresenterImpl(private val resources: Resources) : WelcomeContract.WelcomePresenter {
  private lateinit var view: WelcomeContract.WelcomeView
  private lateinit var trainingFeatures: List<List<Double>>
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
      classify(lbpHist).also {
        if (it == FAKE) view.showError() else view.showContent()
      }
    }
  }
  
  private fun fit() {
    classifier.fit(trainingFeatures, labels)
  }
  
  private fun classify(features: IntArray): String {
    val result = classifier.predict(features)
    if (BuildConfig.DEBUG) Log.d(this::class.java.simpleName, result)
    return result
  }
  
  private fun getLbpHistogram(image: Array<DoubleArray>): IntArray {
    val lbp = Lbp(8, 1)
    val lbpResult = lbp.getLBP(image)
    val lbp1d = lbp.reshape(lbpResult, 0, 149, 0, 149)
    val histogram = lbp.histc(lbp1d)
    printMatrix(lbpResult)
    histogram.forEachIndexed { index, it -> println("histogram[$index]: $it ") }
    return histogram
  }
}