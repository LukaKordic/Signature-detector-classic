package com.example.myapplication.common.utils

import android.content.Context
import androidx.annotation.RawRes
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStreamReader

class ResourceManager(private val context: Context) : Resources {
  
  override fun getRawSignatureFeatures(@RawRes signatureFeatures: Int): List<List<Double>> {
    val featuresInput = context.resources.openRawResource(signatureFeatures)
    return CSVFile(featuresInput).read()
  }
  
  override fun getRawSignatureLabels(@RawRes signatureLabels: Int): List<String> {
    val labelsInput = context.resources.openRawResource(signatureLabels)
    val gson = Gson()
    val reader = BufferedReader(InputStreamReader(labelsInput))
    val stringList = object : TypeToken<List<String>>() {}.type
    return gson.fromJson(reader, stringList)
  }
}