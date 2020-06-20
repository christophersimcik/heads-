package com

import android.graphics.Color
import android.graphics.PointF

class HeadManager {

    val pointsMainColor = Color.RED
    val pointsControlColor = Color.BLUE

    val sizeMainPoints = 12.5f
    val sizeControlPoints = 7.5f

    //main corners
    var mainTopLeft : PointF = PointF()
    var mainTopRight : PointF = PointF()
    var mainBottomRight : PointF = PointF()
    var mainBottomLeft : PointF = PointF()

    // control points
    var controlTopLeft : PointF = PointF()
    var controlTopRight : PointF = PointF()
    var controlBottomRight : PointF = PointF()
    var controlBottomLeft : PointF = PointF()

    // exact points
    var leftTop : PointF = PointF()
    var topLeft : PointF = PointF()
    var topRight : PointF = PointF()
    var rightTop : PointF = PointF()
    var rightBottom : PointF = PointF()
    var bottomRight : PointF = PointF()
    var bottomLeft : PointF = PointF()
    var leftBottom : PointF = PointF()

    lateinit var mainPoints : Array<PointF>
    lateinit var controlPoints :Array<PointF>

    fun invert(){
        var temp : PointF

        temp = leftTop
        leftTop = leftBottom
        leftBottom = temp
        temp = rightTop
        rightTop = rightBottom
        rightBottom = temp
    }

    fun updatePoints(){
        mainPoints = arrayOf(leftTop,topLeft,topRight,rightTop,rightBottom,bottomRight,bottomLeft,leftBottom)
    }

    fun updateControl(){
        controlPoints = arrayOf(controlTopLeft,controlTopRight,controlBottomRight,controlBottomLeft)
    }

    fun calculateDistance(a : PointF, b : PointF) : Float{
        val x = Math.pow((b.x-a.x).toDouble(),2.0)
        val y = Math.pow((b.y-a.y).toDouble(),2.0)
        return Math.sqrt(x+y).toFloat()
    }

}