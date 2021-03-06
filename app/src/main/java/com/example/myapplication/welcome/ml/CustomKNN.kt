package com.example.myapplication.welcome.ml

import kotlin.math.pow
import kotlin.math.sqrt

open class CustomKNN {
  private lateinit var trainingData: List<List<Double>>
  private lateinit var labels: List<String>
  
  fun fit(trainingData: List<List<Double>>, labels: List<String>) {
    this.trainingData = trainingData
    this.labels = labels
  }
  
  fun predict(testData: IntArray): String {
    var bestDistance = distance(testData, trainingData[0])
    var bestIndex = 0
    for (i in 1 until trainingData.size) {
      val dist = distance(testData, trainingData[i])
      println("best distance: $bestDistance, distance at [$i]: $dist")
      if (dist < bestDistance) {
        bestDistance = dist
        bestIndex = i
      }
    }
    return labels[bestIndex]
  }
  
  private fun distance(a: IntArray, b: List<Double>): Double {
    var diffSquareSum = 0.0
    for (i in a.indices) {
      diffSquareSum += (a[i] - b[i]).pow(2.0)
    }
    return sqrt(diffSquareSum)
  }
}