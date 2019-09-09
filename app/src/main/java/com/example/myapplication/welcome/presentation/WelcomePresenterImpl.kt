package com.example.myapplication.welcome.presentation

import android.util.Log
import com.example.myapplication.common.CSVFile
import com.example.myapplication.common.LBP
import com.example.myapplication.common.printMatrix
import com.example.myapplication.welcome.WelcomeContract
import com.example.myapplication.welcome.ml.CustomKNN
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class WelcomePresenterImpl : WelcomeContract.WelcomePresenter {
  private lateinit var view: WelcomeContract.WelcomeView
  private lateinit var trainingFeatures: List<List<Int>>
  private lateinit var labels: List<String>
  private val classifier by lazy { CustomKNN() }
  
  override fun setView(view: WelcomeContract.WelcomeView) {
    this.view = view
  }
  
  override fun capturePhotoClicked() = view.checkCameraPermission()
  
  override fun loadImageClicked() = view.checkReadStoragePermission()
  
  override fun recognizeClicked(image: Array<DoubleArray>) {
    GlobalScope.launch(Dispatchers.IO) {
      val lbp = LBP(8, 1)
      val lbpResult = lbp.getLBP(image)
      val lbp1d = lbp.reshape(lbpResult, 0, 148, 0, 148)
      val cutPoints = doubleArrayOf(0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0)
      val histogram = lbp.histc(lbp1d, cutPoints)
//      printMatrix(lbpResult)

//      histogram.forEachIndexed { index, it -> println("histogram[$index]: $it ") }
      classify(histogram.toList())
    }
  }
  
  override fun parseTrainingFeatures(inputStream: InputStream) {
    GlobalScope.launch(Dispatchers.IO) {
      trainingFeatures = CSVFile(inputStream).read()
      fit()
    }
  }
  
  override fun parseTrainingLabels(inputStream: InputStream) {
    GlobalScope.launch(Dispatchers.IO) {
      val gson = Gson()
      val reader = BufferedReader(InputStreamReader(inputStream))
      val stringList = object : TypeToken<List<String>>() {}.type
      labels = gson.fromJson(reader, stringList)
      println(labels)
    }
  }
  
  private fun fit() {
    classifier.fit(trainingFeatures, labels)
  }
  
  private fun classify(features: List<Double>) {
    val result = classifier.predict(features)
    Log.d(this::class.java.simpleName, result)
  }
}