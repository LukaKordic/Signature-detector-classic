package com.example.myapplication.welcome.ml

import com.example.myapplication.common.constants.FAKE
import com.example.myapplication.common.constants.ORIGINAL
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
    val bestDistances = DoubleArray(5) { distance(testData, trainingData[it]) }
    val bestDistanceIndices = IntArray(5) { it }
    var originalCounter = 0
    bestDistances.sort()
    bestDistanceIndices.sort()
    
    for (i in 5 until trainingData.size) {
      val dist = distance(testData, trainingData[i])
      for (index in bestDistances.indices) {
        if (dist < bestDistances[index]) {
          bestDistances[index] = dist
          bestDistanceIndices[index] = i
          break
        }
      }
    }
    bestDistanceIndices.forEach {
      if (labels[it] == ORIGINAL) {
        originalCounter++
      }
    }
    return if (originalCounter > 2) ORIGINAL else FAKE
  }
  
  private fun distance(a: IntArray, b: List<Double>): Double {
    var diffSquareSum = 0.0
    for (i in a.indices) {
      diffSquareSum += (a[i] - b[i]).pow(2)
    }
    return sqrt(diffSquareSum)
  }
}