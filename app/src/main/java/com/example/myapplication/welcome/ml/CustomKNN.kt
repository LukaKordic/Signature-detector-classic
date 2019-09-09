package com.example.myapplication.welcome.ml

import kotlin.math.pow
import kotlin.math.sqrt

class CustomKNN {
  private lateinit var trainingData: Array<DoubleArray>
  private lateinit var labels: Array<String>
  
  fun fit(trainingData: Array<DoubleArray>, labels: Array<String>) {
    this.trainingData = trainingData
    this.labels = labels
  }
  
  fun predict(features: DoubleArray): String {
    var bestDistance = distance(features, trainingData[0])
    var bestIndex = 0
    for (i in 1..trainingData.size) {
      val dist = distance(features, trainingData[i])
      if (dist < bestDistance) {
        bestDistance = dist
        bestIndex = i
      }
    }
    return labels[bestIndex]
  }
  
  private fun distance(a: DoubleArray, b: DoubleArray): Double {
    var diffSquareSum = 0.0
    for (i in a.indices) {
      diffSquareSum += (a[i] - b[i]).pow(2.0)
    }
    return sqrt(diffSquareSum)
  }
}