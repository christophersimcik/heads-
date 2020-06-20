package com

import android.graphics.PointF

class EyeManager(head : HeadNew) {
    val head = head
    var horizontalOffset = 0.0f
    var verticalOffset = 0.0f

    var leftCenter = PointF()
    var rightCenter = PointF()

    var leftPoints = arrayListOf<PointF>()
    var rightPoints = arrayListOf<PointF>()

    fun updatePoints() {
        val bounds = head.getBounds()
        leftCenter.x = bounds.centerX() - bounds.width()/2*horizontalOffset
        leftCenter.y = bounds.bottom - bounds.height() * verticalOffset
        rightCenter.x = bounds.centerX() + bounds.width()/2*horizontalOffset
        rightCenter.y = bounds.bottom - bounds.height() * verticalOffset
    }

    fun decribeRectangle(){

    }

    fun describeTriangle(){

    }


}