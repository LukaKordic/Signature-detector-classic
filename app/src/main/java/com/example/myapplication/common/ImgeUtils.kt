package com.example.myapplication.common

import android.graphics.*
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.DecimalFormat

fun convertImageTo2DArray(image: Bitmap): Array<DoubleArray> {
  val imageMatrix = Array(150) { DoubleArray(150) }
  val resizedImage = resizeImage(image)
  val f = DecimalFormat("0.#")
  
  GlobalScope.launch {
    for (x in 0 until IMG_WIDTH) {
      for (y in 0 until IMG_HEIGHT) {
        val pixelValue = resizedImage.getPixel(x, y)
        val red = pixelValue.red
        val green = pixelValue.green
        val blue = pixelValue.blue
        val gray = (red + green + blue) / 3
        
        imageMatrix[x][y] = gray.toDouble()
//        print(f.format(imageMatrix[x][y]) + "\t")
      }
//      println()
    }
  }
  return imageMatrix
}

fun convert2dArrayToBitmap(array: Array<DoubleArray>): Bitmap {
  val imageBitmap = Bitmap.createBitmap(IMG_WIDTH, IMG_HEIGHT, Bitmap.Config.ARGB_8888)
  for (x in 0 until IMG_WIDTH) {
    for (y in 0 until IMG_HEIGHT) {
      val color = array[x][y]
      imageBitmap.setPixel(x, y, color.toInt())
    }
  }
  return imageBitmap
}

private fun resizeImage(image: Bitmap): Bitmap {
  return Bitmap.createScaledBitmap(image, IMG_WIDTH, IMG_HEIGHT, false)
}

fun Bitmap.toGrayscale(): Bitmap {
  val width = this.width
  val height = this.height
  
  val grayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
  val canvas = Canvas(grayscale)
  val paint = Paint()
  val colorMatrix = ColorMatrix()
  colorMatrix.setSaturation(0f)
  val filter = ColorMatrixColorFilter(colorMatrix)
  paint.colorFilter = filter
  canvas.drawBitmap(grayscale, 0f, 0f, paint)
  return grayscale
}

fun printMatrix(matrix: Array<ByteArray>) {
  val f = DecimalFormat("0.#")
  for (x in matrix.indices) {
    for (y in 0 until matrix[x].size) {
      print(f.format(matrix[x][y]) + "\t")
    }
    println()
  }
}

//IMAGE DIMENSIONS
private const val IMG_WIDTH = 150
private const val IMG_HEIGHT = 150