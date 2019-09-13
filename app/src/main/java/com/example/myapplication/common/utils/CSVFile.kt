package com.example.myapplication.common.utils

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class CSVFile(private var inputStream: InputStream) {
  
  fun read(): List<List<Double>> {
    val resultList = mutableListOf<List<String>>()
    val doubles = mutableListOf<List<Double>>()
    val rows = mutableListOf<String>()
    val reader = BufferedReader(InputStreamReader(inputStream))
    inputStream.use {
      try {
        repeat(470) {
          rows.add(reader.readLine())
        }
        rows.forEach { resultList.add(it.split(",")) }
      } catch (ex: IOException) {
        throw RuntimeException("Error in reading CSV file: $ex")
      }
    }
    
    resultList.forEach { it.map { it.toDouble() }.also { doubles.add(it) } }
//    doubles.forEach { println(it) }
    return doubles
  }
}