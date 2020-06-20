package com

import android.graphics.Color
import android.graphics.PointF
import android.graphics.RectF
import kotlinx.android.synthetic.main.test_layout.view.*

class MouthManager(head: HeadNew) {

    val head = head

    val upperControlColor = Color.RED
    val lowerControlColor = Color.BLUE

    var upperControlSize = 25.0f
    var lowerControlSize = 25.0f

    var verticalOffset = 0.0f
    var horizontalOffset = 0.0f

    var upperOffset = 10f
        set(upperOffset) {
            setUpperControl(upperOffset)
        }
    var lowerOffset = 10f
        set(lowerOffset) {
            setLowerControl(lowerOffset)
        }

    //main corners
    var mainLeft: PointF = PointF()
    var mainRight: PointF = PointF()

    // control points
    var controlUpper: PointF = PointF()
    var controlLower: PointF = PointF()

    fun updatePoint(x: Float, y: Float) {

        val bounds = head.getBounds()

        horizontalOffset = x
        verticalOffset = y

        if (horizontalOffset > bounds.width() / 2) {
            horizontalOffset = bounds.width() / 2
        }
        if (verticalOffset < bounds.top) {
            verticalOffset = bounds.top
        }
        if (verticalOffset > bounds.bottom) {
            verticalOffset = bounds.bottom
        }

        val x = bounds.centerX()
        val y = bounds.centerY() + verticalOffset

        mainLeft.x = x - horizontalOffset
        mainLeft.y = y
        mainRight.x = x + horizontalOffset
        mainRight.y = y

        updateControls(y)

    }

    private fun updateControls(y: Float) {

        // check if controls are within head's bounds
        val top = head.getBounds().top
        val bottom = head.getBounds().bottom

        val upperY = y + upperOffset
        val lowerY = y + lowerOffset

        while (upperY < top) {
            upperOffset++
        }

        // limit upper control y to top or bottom
        setUpperControl(upperOffset)

        // limit lower control y to top or bottom
        setLowerControl(lowerOffset)
    }

    fun setUpperControl(offset: Float) {
        val bounds = head.getBounds()
        val y = controlUpper.y
        if (y + offset >= bounds.top && y + offset <= bounds.bottom) {
            controlUpper.y += offset
        }
        // maintain order
        while (controlUpper.y > controlLower.y) {
            lowerOffset++
            setLowerControl(lowerOffset)
        }
    }

    fun setLowerControl(offset: Float) {
        val bounds = head.getBounds()
        val y = controlLower.y
        if (y + offset >= bounds.top && y + offset <= bounds.bottom) {
            controlLower.y += offset
        }

        //maintain order
        while (controlLower.y < controlUpper.y) {
            upperOffset--
            setUpperControl(upperOffset)
        }
    }
}