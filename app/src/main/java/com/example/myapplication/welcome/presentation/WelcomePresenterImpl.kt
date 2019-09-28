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

val fakePredictionSample = intArrayOf(1492,
                                  192,
                                  47,
                                  74,
                                  824,
                                  58,
                                  118,
                                  175,
                                  41,
                                  4,
                                  4,
                                  5,
                                  126,
                                  9,
                                  54,
                                  72,
                                  223,
                                  117,
                                  7,
                                  31,
                                  106,
                                  22,
                                  15,
                                  41,
                                  92,
                                  33,
                                  4,
                                  25,
                                  354,
                                  33,
                                  117,
                                  382,
                                  35,
                                  11,
                                  2,
                                  9,
                                  28,
                                  4,
                                  3,
                                  8,
                                  5,
                                  1,
                                  0,
                                  0,
                                  14,
                                  1,
                                  4,
                                  8,
                                  98,
                                  29,
                                  5,
                                  16,
                                  61,
                                  3,
                                  12,
                                  16,
                                  131,
                                  29,
                                  13,
                                  47,
                                  386,
                                  25,
                                  167,
                                  210,
                                  726,
                                  92,
                                  37,
                                  49,
                                  332,
                                  18,
                                  58,
                                  49,
                                  19,
                                  3,
                                  4,
                                  2,
                                  53,
                                  2,
                                  48,
                                  8,
                                  52,
                                  9,
                                  1,
                                  3,
                                  8,
                                  1,
                                  5,
                                  4,
                                  39,
                                  4,
                                  1,
                                  8,
                                  75,
                                  3,
                                  14,
                                  40,
                                  90,
                                  14,
                                  4,
                                  13,
                                  46,
                                  5,
                                  18,
                                  12,
                                  8,
                                  3,
                                  2,
                                  2,
                                  27,
                                  2,
                                  15,
                                  14,
                                  170,
                                  32,
                                  12,
                                  10,
                                  43,
                                  3,
                                  9,
                                  19,
                                  215,
                                  23,
                                  29,
                                  43,
                                  421,
                                  16,
                                  150,
                                  158,
                                  31,
                                  77,
                                  5,
                                  92,
                                  15,
                                  38,
                                  6,
                                  183,
                                  2,
                                  7,
                                  0,
                                  6,
                                  5,
                                  12,
                                  5,
                                  79,
                                  2,
                                  25,
                                  1,
                                  26,
                                  2,
                                  9,
                                  2,
                                  11,
                                  5,
                                  9,
                                  0,
                                  24,
                                  18,
                                  17,
                                  10,
                                  163,
                                  4,
                                  7,
                                  1,
                                  6,
                                  3,
                                  6,
                                  3,
                                  22,
                                  2,
                                  1,
                                  1,
                                  5,
                                  3,
                                  2,
                                  3,
                                  22,
                                  5,
                                  23,
                                  1,
                                  22,
                                  1,
                                  6,
                                  1,
                                  31,
                                  5,
                                  26,
                                  2,
                                  125,
                                  33,
                                  48,
                                  21,
                                  742,
                                  95,
                                  231,
                                  4,
                                  225,
                                  52,
                                  57,
                                  29,
                                  336,
                                  8,
                                  9,
                                  0,
                                  29,
                                  21,
                                  17,
                                  15,
                                  137,
                                  6,
                                  25,
                                  1,
                                  20,
                                  0,
                                  1,
                                  4,
                                  15,
                                  7,
                                  11,
                                  0,
                                  39,
                                  19,
                                  20,
                                  12,
                                  133,
                                  69,
                                  85,
                                  3,
                                  109,
                                  32,
                                  7,
                                  14,
                                  103,
                                  2,
                                  7,
                                  3,
                                  30,
                                  16,
                                  14,
                                  31,
                                  194,
                                  58,
                                  440,
                                  5,
                                  144,
                                  10,
                                  25,
                                  5,
                                  104,
                                  80,
                                  138,
                                  8,
                                  626,
                                  139,
                                  142,
                                  215,
                                  6321)