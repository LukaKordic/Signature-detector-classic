package com.example.myapplication.welcome.presentation

import android.graphics.Bitmap
import android.util.Log
import com.example.myapplication.common.CSVFile
import com.example.myapplication.common.LBP
import com.example.myapplication.common.convertImageTo2DArray
import com.example.myapplication.welcome.WelcomeContract
import com.example.myapplication.welcome.ml.CustomKNN
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class WelcomePresenterImpl : WelcomeContract.WelcomePresenter {
  private lateinit var view: WelcomeContract.WelcomeView
  private lateinit var trainingFeatures: List<List<Double>>
  private lateinit var labels: List<String>
  private val classifier by lazy { CustomKNN() }
  
  override fun setView(view: WelcomeContract.WelcomeView) {
    this.view = view
  }
  
  override fun capturePhotoClicked() = view.checkCameraPermission()
  
  override fun loadImageClicked() = view.checkReadStoragePermission()
  
  override fun recognizeClicked(image: Bitmap) {
    GlobalScope.launch(Dispatchers.IO) {
      val image2D = convertImageTo2DArray(image)
      val lbp = LBP(8, 1)
      val lbpResult = lbp.getLBP(image2D)
      val test = lbp.reshape(lbpResult, 0, 148, 0, 148)
      val histArray = lbp.histc(test)
//    printMatrix(lbpResult)
      
      classify(histArray.toList())
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