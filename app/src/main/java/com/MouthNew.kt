package com

import android.graphics.*
import com.google.android.play.core.internal.r

class MouthNew(val head : HeadNew) : BodyPart() {


    var active : Boolean = false

    val mouthManager = MouthManager(head)
    var mColor : Int = Color.WHITE
    val mPaint = Paint()

    fun drawPath(canvas: Canvas) {
        mPaint.color = mColor
        canvas.drawPath(makePath(), mPaint)
    }

    fun drawControllers(canvas: Canvas) {
        var x : Float
        var y : Float
        mPaint.color = mouthManager.upperControlColor
        x  = mouthManager.controlUpper.x
        y = mouthManager.controlUpper.y - 10.0f
        canvas.drawCircle(x,y,mouthManager.upperControlSize,mPaint)
        mPaint.color = mouthManager.lowerControlColor
        x = mouthManager.controlLower.x
        y = mouthManager.controlLower.y + 10.0f
        canvas.drawCircle(x,y,mouthManager.lowerControlSize,mPaint)

    }

    private fun makePath() : Path {
        val lips = Path()
        lips.moveTo(
            mouthManager.mainLeft.x,
            mouthManager.mainLeft.y
        )
        lips.quadTo(
            mouthManager.controlUpper.x,
            mouthManager.controlUpper.y,
            mouthManager.mainRight.x,
            mouthManager.mainRight.y
        )
        lips.quadTo(
            mouthManager.controlLower.x,
            mouthManager.controlLower.y,
            mouthManager.mainLeft.x,
            mouthManager.mainLeft.y
        )
        lips.close()
        return lips
    }

    fun updatePositon(x : Float, y : Float){
        mouthManager.updatePoint(x,y)
    }

    fun updateUpperLip(y : Float){
        mouthManager.setUpperControl(y)
    }

    fun updateLowerLip(y : Float){
        mouthManager.setLowerControl(y)
    }
}