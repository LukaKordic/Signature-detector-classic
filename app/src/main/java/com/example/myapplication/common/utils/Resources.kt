package com.example.myapplication.common.utils

import androidx.annotation.RawRes

interface Resources {
  
  fun getRawSignatureFeatures(@RawRes signatureFeatures: Int): List<List<Double>>
  
  fun getRawSignatureLabels(@RawRes signatureLabels: Int): List<String>
}