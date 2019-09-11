package com.example.myapplication.common

import androidx.annotation.RawRes

interface Resources {
  
  fun getRawSignatureFeatures(@RawRes signatureFeatures: Int): List<List<Int>>
  
  fun getRawSignatureLabels(@RawRes signatureLabels: Int): List<String>
}