package com.example.myapplication.common
//
//import android.graphics.*
//import android.util.Log
//import org.opencv.core.Mat
//
//class LocalBinaryPatterns {
//  private val points = 8
//  private val radius = 1
//  private val imageMat = Mat()
//
//  fun computeLbp(image: Bitmap) {
//    val img = resizeImage(image)
//    val output = Array(150) { IntArray(150) }
//
//    //find patterns
//    for (x in 1 until 149) {
//      for (y in 1 until 149) {
//        var code = 0
//        val center = img.getPixel(x, y)
//        code = if (center >= img.getPixel(x - 1, y - 1)) (code or 1) shl 7 else (code or 0) shl 7
//        code = if (center >= img.getPixel(x - 1, y)) (code or 1) shl 6 else (code or 0) shl 6
//        code = if (center >= img.getPixel(x - 1, y + 1)) (code or 1) shl 5 else (code or 0) shl 5
//        code = if (center >= img.getPixel(x, y + 1)) (code or 1) shl 4 else (code or 0) shl 4
//        code = if (center >= img.getPixel(x + 1, y + 1)) (code or 1) shl 3 else (code or 0) shl 3
//        code = if (center >= img.getPixel(x + 1, y)) (code or 1) shl 2 else (code or 0) shl 2
//        code = if (center >= img.getPixel(x + 1, y - 1)) (code or 1) shl 1 else (code or 0) shl 1
//        code = if (center >= img.getPixel(x, y - 1)) (code or 1) shl 0 else (code or 0) shl 0
//
//        output[x][y] = code
//        Log.d("Luka", code.toString())
//      }
//    }
//  }
//}
//
//
