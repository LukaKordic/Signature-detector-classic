package com.example.myapplication.common

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class CSVFile(private var inputStream: InputStream) {
  
  fun read(): List<List<Int>> {
    val resultList = mutableListOf<List<String>>()
    val rows = mutableListOf<String>()
    val reader = BufferedReader(InputStreamReader(inputStream))
    inputStream.use {
      try {
        repeat(471) {
          rows.add(reader.readLine())
        }
        rows.forEach { resultList.add(it.split(",")) }
      } catch (ex: IOException) {
        throw RuntimeException("Error in reading CSV file: $ex")
      }
    }
    
    val doubles = mutableListOf<List<Int>>()
    resultList.forEach { it.map { it.toInt() }.also { doubles.add(it) } }
//    doubles.forEach { println(it) }
    return doubles
  }
}