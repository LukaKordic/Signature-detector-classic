package com.example.myapplication.welcome.ml

abstract class RegionGrow {
  companion object {
    fun findStatic(matrix: Array<Array<ByteArray>>): Array<IntArray> {
      val temp = Array(matrix.size * matrix[0].size * matrix[0][0].size) { IntArray(3) }
      var found = 0
      for (i in matrix.indices) {
        for (j in matrix[i].indices) {
          for (k in matrix[i][j].indices) {
            if (matrix[i][j][k] > 0) {
              temp[found][0] = i
              temp[found][1] = j
              temp[found][2] = k
              ++found
            }
          }
        }
      }
      val indices = Array(found) { IntArray(3) }
      for (i in 0 until found) {
        for (j in 0..2) {
          indices[i][j] = temp[i][j]
        }
      }
      return indices
    }
  }
}