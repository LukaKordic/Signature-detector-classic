package com.example.myapplication.common

class LBP(private val samples: Int, private val radius: Int) {
  private val mapping: DoubleArray
  private val neighbourhood: Array<DoubleArray>
  var cutPoints: DoubleArray = DoubleArray(samples + 2)
  
  init {
    for (i in 0 until samples + 2) {
      cutPoints[i] = i.toDouble()
    }
    mapping = getMapping(samples)
    println("Mapping length " + mapping.size + " samples " + samples)
    neighbourhood = getCircularNeighbourhood(radius, samples)
    
  }
  
  fun getLBP(data: Array<DoubleArray>): Array<DoubleArray> {
    val width = data.size
    val height = data[0].size
    val lbpSlice = Array(width) { DoubleArray(height) }
    /*Calculate the lbp*/
    val coordinates = IntArray(2)
    for (i in 0 + radius until width - radius) {
      for (j in 0 + radius until height - radius) {
        coordinates[0] = i
        coordinates[1] = j
        lbpSlice[i][j] = lbpBlock(data, coordinates)
      }
    }
    
    return lbpSlice
  }
  
  fun getLbpHist(data: Array<IntArray>): IntArray {
    val histogram = IntArray(10)
    for (i in data.indices) {
      for (j in data[0].indices) {
        when (data[i][j]) {
          0 -> histogram[0] = histogram[0] + 1
          1 -> histogram[1] = histogram[1] + 1
          2 -> histogram[2] = histogram[2] + 1
          3 -> histogram[3] = histogram[3] + 1
          4 -> histogram[4] = histogram[4] + 1
          5 -> histogram[5] = histogram[5] + 1
          6 -> histogram[6] = histogram[6] + 1
          7 -> histogram[7] = histogram[7] + 1
          8 -> histogram[8] = histogram[8] + 1
          9 -> histogram[9] = histogram[9] + 1
        }
      }
    }
    return histogram
  }
  
  private fun lbpBlock(data: Array<DoubleArray>, coordinates: IntArray): Double {
    var lbpValue = 0
    val x = coordinates[0].toDouble()
    val y = coordinates[1].toDouble()
    for (i in neighbourhood.indices) {
      lbpValue = if (data[x.toInt()][y.toInt()] > getBicubicInterpolatedPixel(x + neighbourhood[i][0],
                                                                              y + neighbourhood[i][1],
                                                                              data) + 3.0) lbpValue or (1 shl i) else lbpValue and (1 shl i).inv()
    }
    return mapping[lbpValue]
  }
  
  /*LBP histogram functions*/
  fun histc(values: DoubleArray, cutPoints: DoubleArray): DoubleArray {
    val histogram = DoubleArray(cutPoints.size)
    for (i in values.indices) {
      var j = 0
      
      //while (j < cutPoints.length-1 && values[i] <cutPoints[j+1]){++j;}
      while (j < cutPoints.size - 2 && values[i] >= cutPoints[j + 1]) {
        ++j
      }
      if (values[i] == cutPoints[cutPoints.size - 1]) {
        j += 1
      }
      //System.out.println("ind "+i+ " val "+values[i]+" bin "+j+" from "+cutPoints[j]);
      histogram[j] += 1.0
    }
    
    return histogram
  }
  
  fun histc(values: DoubleArray): DoubleArray {
    var histogram = DoubleArray(cutPoints.size)
    for (i in values.indices) {
      var j = 0
      
      //while (j < cutPoints.length-1 && values[i] <cutPoints[j+1]){++j;}
      while (j < cutPoints.size - 2 && values[i] >= cutPoints[j + 1]) {
        ++j
      }
      if (values[i] == cutPoints[cutPoints.size - 1]) {
        j = j + 1
      }
//      println("ind " + i + " val " + values[i] + " bin " + j + " from " + cutPoints[j])
      histogram[j] += 1.0
    }
    histogram = arrDiv(histogram, sum(histogram))
    return histogram
  }
  
  /*reshape 2D Matrix*/
  fun reshape(dataIn: Array<DoubleArray>, xb: Int, xe: Int, yb: Int, ye: Int): DoubleArray {
    val array = DoubleArray((xe - xb + 1) * (ye - yb + 1))
    var ind = 0
    for (y in yb..ye) {
      for (x in xb..xe) {
        array[ind] = dataIn[x][y]
        ++ind
      }
    }
    return array
  }
  
  private fun sum(arrayIn: DoubleArray): Double {
    var temp = 0.0
    for (i in arrayIn.indices) {
      temp += arrayIn[i]
    }
    return temp
  }
  
  private fun arrDiv(arrayIn: DoubleArray, divisor: Double): DoubleArray {
    for (i in arrayIn.indices) {
      arrayIn[i] /= divisor
    }
    return arrayIn
  }
  
  fun checkClose(sampleHist: DoubleArray, modelHist: DoubleArray): Double {
    var closeness = 0.0
    for (h in sampleHist.indices) {
      closeness += min(sampleHist[h], modelHist[h])
    }
    return closeness
  }
  
  private fun min(a: Double, b: Double): Double {
    return if (a < b) a else b
  }
  
  companion object {
    
    /*Mapping to rotation invariant uniform patterns: riu2 in getmapping.m*/
    private fun getMapping(samples: Int): DoubleArray {
      val bitMaskLength = Math.pow(2.0, samples.toDouble()).toInt()
      val table = DoubleArray(bitMaskLength)
      var j: Int
      var sampleBitMask = 0
      for (i in 0 until samples) {
        sampleBitMask = sampleBitMask or (1 shl i)
      }
      
      var numt: Int
      for (i in 0 until bitMaskLength) {
        j = i shl 1 and sampleBitMask //j = bitset(bitshift(i,1,samples),1,bitget(i,samples)); %rotate left
        j = if (i shr samples - 1 > 0) j or 1 else j and 1.inv()  //Set first bit to one or zero
        numt = 0
        for (k in 0 until samples) {
          numt += i xor j shr k and 1
        }
        
        if (numt <= 2) {
          for (k in 0 until samples) {
            table[i] = table[i].plus(i shr k and 1)
          }
        } else {
          table[i] = (samples + 1).toDouble()
        }
      }
      return table
    }
    
    /*Neighbourhood coordinates relative to pixel of interest*/
    private fun getCircularNeighbourhood(radius: Int, samples: Int): Array<DoubleArray> {
      val samplingCoordinates = Array(samples) { DoubleArray(2) }
      val angleIncrement = Math.PI * 2.0 / samples.toDouble()
      for (n in 0 until samples) {
        samplingCoordinates[n][0] = radius.toDouble() * Math.cos(n.toDouble() * angleIncrement)
        samplingCoordinates[n][1] = radius.toDouble() * Math.sin(n.toDouble() * angleIncrement)
      }
      return samplingCoordinates
    }
    
    /** This method is from Chapter 16 of "Digital Image Processing:
     * An Algorithmic Introduction Using Java" by Burger and Burge
     * (http://www.imagingbook.com/).  */
    fun getBicubicInterpolatedPixel(x0: Double, y0: Double, data: Array<DoubleArray>): Double {
      val u0 = Math.floor(x0).toInt()  //use floor to handle negative coordinates too
      val v0 = Math.floor(y0).toInt()
      val width = data.size
      val height = data[0].size
      if (u0 < 1 || u0 > width - 3 || v0 < 1 || v0 > height - 3) {
        if ((u0 == 0 || u0 < width - 1) && (v0 == 0 || v0 < height - 1)) { /*Use bilinear interpolation http://en.wikipedia.org/wiki/Bilinear_interpolation*/
          val x = x0 - u0.toDouble()
          val y = y0 - v0.toDouble()
          return (data[u0][v0] * (1 - x) * (1 - y)  /*f(0,0)(1-x)(1-y)*/
              + data[u0 + 1][v0] * (1 - y) * x  /*f(1,0)x(1-y)*/
              + data[u0][v0 + 1] * (1 - x) * y  /*f(0,1)(1-x)y*/
              + data[u0 + 1][v0 + 1] * x * y)  /*f(1,1)xy*/
        }
        return 0.0 /*Return zero for points outside the interpolable area*/
      }
      var q = 0.0
      for (j in 0..3) {
        val v = v0 - 1 + j
        var p = 0.0
        for (i in 0..3) {
          val u = u0 - 1 + i
          p = p + data[u][v] * cubic(x0 - u)
        }
        q = q + p * cubic(y0 - v)
      }
      return q
    }
    
    fun cubic(x: Double): Double {
      var x = x
      val a = 0.5 // Catmull-Rom interpolation
      if (x < 0.0) x = -x
      var z = 0.0
      if (x < 1.0)
        z = x * x * (x * (-a + 2.0) + (a - 3.0)) + 1.0
      else if (x < 2.0)
        z = -a * x * x * x + 5.0 * a * x * x - 8.0 * a * x + 4.0 * a
      return z
    }
    
    /*reshape 3D Matrix*/
    fun reshape(dataIn: Array<Array<DoubleArray>>, xb: Int, xe: Int, yb: Int, ye: Int, db: Int, de: Int): DoubleArray {
      val array = DoubleArray((xe - xb + 1) * (ye - yb + 1) * (de - db + 1))
      var ind = 0
      for (d in db..de) {
        for (y in yb..ye) {
          for (x in xb..xe) {
            array[ind] = dataIn[x][y][d]
            ++ind
          }
        }
      }
      return array
    }
    
    /*reshape 3D Matrix*/
    fun reshape(dataIn: Array<Array<DoubleArray>>, mask: Array<Array<ByteArray>>): DoubleArray {
      val indices = RegionGrow.findStatic(mask)
      val array = DoubleArray(indices.size)
      for (i in indices.indices) {
        array[i] = dataIn[indices[i][0]][indices[i][1]][indices[i][2]]
      }
      return array
    }

//    @JvmStatic
//    fun main(ar: Array<String>) {
//
//      val data = arrayOf(doubleArrayOf(1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0),
//                         doubleArrayOf(1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0),
//                         doubleArrayOf(1.0, 1.0, 1.0, 1.0, 1.0, 5.0, 5.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0),
//                         doubleArrayOf(1.0, 1.0, 1.0, 1.0, 1.0, 5.0, 5.0, 5.0, 1.0, 1.0, 1.0, 1.0, 1.0),
//                         doubleArrayOf(1.0, 1.0, 1.0, 1.0, 1.0, 5.0, 5.0, 5.0, 5.0, 1.0, 1.0, 1.0, 1.0),
//                         doubleArrayOf(1.0, 1.0, 1.0, 1.0, 1.0, 5.0, 5.0, 5.0, 5.0, 5.0, 1.0, 1.0, 1.0),
//                         doubleArrayOf(1.0, 1.0, 1.0, 1.0, 1.0, 5.0, 5.0, 5.0, 5.0, 5.0, 1.0, 1.0, 1.0),
//                         doubleArrayOf(1.0, 1.0, 1.0, 1.0, 1.0, 5.0, 5.0, 5.0, 5.0, 5.0, 1.0, 1.0, 1.0),
//                         doubleArrayOf(1.0, 1.0, 1.0, 1.0, 1.0, 5.0, 5.0, 5.0, 5.0, 5.0, 1.0, 1.0, 1.0),
//                         doubleArrayOf(1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0),
//                         doubleArrayOf(1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0),
//                         doubleArrayOf(1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0),
//                         doubleArrayOf(1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 5.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0),
//                         doubleArrayOf(1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0),
//                         doubleArrayOf(1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0),
//                         doubleArrayOf(1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0))
//      printMatrix(data)
//
//      val lbp = LBP(16, 2)
//      lbp.neighbourhood
//      println("Neighbourhood")
//      printMatrix(lbp.neighbourhood)
//      val resultLBP = lbp.getLBP(data)
//
//      println("VarianceImage")
//      printMatrix(resultLBP)
//    }

//    fun printMatrix(matrix: Array<DoubleArray>) {
//      val f = DecimalFormat("0.#")
//      for (x in matrix.indices) {
//        for (y in 0 until matrix[x].size) {
//          print(f.format(matrix[x][y]) + "\t")
//        }
//        println()
//      }
//    }
//
//    fun printMatrix(matrix: Array<ByteArray>) {
//      for (x in matrix.indices) {
//        for (y in 0 until matrix[x].size) {
//          print(matrix[x][y].toString() + "\t")
//        }
//        println()
//      }
//    }
//  }
  }
}