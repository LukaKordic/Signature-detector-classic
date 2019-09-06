package com.example.myapplication.common

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class CSVFile(private var inputStream: InputStream) {
  
  fun read(): List<List<String>> {
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
    resultList.forEach { println(it) }
    return resultList
  }
}