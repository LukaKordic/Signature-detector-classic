package com.example.myapplication.common.utils

import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import java.text.DecimalFormat

fun convertImageTo2DArray(image: Bitmap): Array<DoubleArray> {
  val imageMatrix = Array(150) { DoubleArray(150) }
  val resizedImage = resizeImage(image).toGrayscale()
  for (y in 0 until IMG_HEIGHT) {
    for (x in 0 until IMG_WIDTH) {
      val pixelValue = resizedImage.getPixel(x, y)
      val red = pixelValue.red
      val green = pixelValue.green
      val blue = pixelValue.blue
      val gray = (red + green + blue) / 3
      imageMatrix[y][x] = gray.toDouble()
    }
  }
  return imageMatrix
}

private fun resizeImage(image: Bitmap): Bitmap {
  return Bitmap.createScaledBitmap(image,
                                   IMG_WIDTH,
                                   IMG_HEIGHT, false)
}

fun Bitmap.toGrayscale(): Bitmap {
  // constant factors
  val GS_RED = 0.299
  val GS_GREEN = 0.587
  val GS_BLUE = 0.114
  
  // create output bitmap
  val bmOut = Bitmap.createBitmap(width, height, config)
  // pixel information
  var A: Int
  var R: Int
  var G: Int
  var B: Int
  var pixel: Int
  
  // scan through every single pixel
  for (x in 0 until width) {
    for (y in 0 until height) {
      // get one pixel color
      pixel = getPixel(x, y)
      // retrieve color of all channels
      A = Color.alpha(pixel)
      R = Color.red(pixel)
      G = Color.green(pixel)
      B = Color.blue(pixel)
      // take conversion up to one single value
      B = (GS_RED * R + GS_GREEN * G + GS_BLUE * B).toInt()
      G = B
      R = G
      // set new pixel color to output bitmap
      bmOut.setPixel(x, y, Color.argb(A, R, G, B))
    }
  }
  // return final image
  return bmOut
}

fun printMatrix(matrix: Array<DoubleArray>) {
  val f = DecimalFormat("0.#")
  for (x in matrix.indices) {
    for (element in matrix[x]) {
      print(f.format(element) + "\t")
    }
    println()
  }
}

//IMAGE DIMENSIONS
private const val IMG_WIDTH = 150
private const val IMG_HEIGHT = 150