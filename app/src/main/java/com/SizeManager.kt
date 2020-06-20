package com

import android.util.SizeF

class SizeManager(size : SizeF){

    val size : SizeF = size

    val min : Float
    val max : Float

    val centerX : Float
    val centerY : Float

    val minXmin : Float
    val minXmax : Float
    val maxXmin : Float
    val maxXmax : Float
    val minYmin : Float
    val minYmax : Float
    val maxYmin : Float
    val maxYmax : Float

    init {
        max = Math.min(size.width,size.height)
        min = max / 8.0f

        centerX = size.width/2.0f
        centerY = size.width/2.0f

        minXmin = centerX - min/2.0f
        minXmax = centerX + min/2.0f
        maxXmin = centerX - max/2.0f
        maxXmax = centerX + max/2.0f
        minYmin = centerY - min/2.0f
        minYmax = centerY + min/2.0f
        maxYmin = centerY - max/2.0f
        maxYmax = centerY + max/2.0f
    }
}