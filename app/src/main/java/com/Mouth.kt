package com

import android.graphics.*
import com.google.android.play.core.internal.r

class Mouth() : BodyPart() {


    var active : Boolean = false
    var upperLipHeight = 50f
    var lowerLipHeight = 30f
    var color = Color.WHITE
    val paint : Paint = Paint()
    val paintUpperContol = Paint().apply {this.color = Color.RED  }
    val paintLowerContol = Paint().apply {this.color = Color.BLUE  }
    var left = Point()
    var right = Point()
    var relativeHeight = 0f
    var relativeWidth = 0
    var lips = Path();
    var upperControl = Point()
    var lowerControl = Point()

        fun drawPath(canvas : Canvas){
        paint.setColor(color)
        canvas.drawPath(makeLips(),paint)
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeWidth = 0f
        paint.color = Color.WHITE
        canvas.drawPath(makeLips(),paint)
        paint.style = Paint.Style.FILL
        paint.setColor(color)
    }

    fun drawLipControlPoints(canvas : Canvas){
        canvas.drawCircle(upperControl.x.toFloat(),upperControl.y.toFloat(),50f, paintUpperContol)
        canvas.drawCircle(lowerControl.x.toFloat(),lowerControl.y.toFloat(),50f, paintLowerContol)
    }

    private fun makeLips() : Path {
        val lips = Path()
        lips.moveTo(left.x.toFloat(),left.y.toFloat())
        lips.quadTo(upperControl.x.toFloat(),upperControl.y.toFloat(),right.x.toFloat(),right.y.toFloat())
        lips.quadTo(lowerControl.x.toFloat(),lowerControl.y.toFloat(),left.x.toFloat(),left.y.toFloat())
        lips.close()
        this.lips = lips
        return lips
    }

    fun adjustUpperControl(top : Int, bottom : Int, position : Int){
        if(position > top+1 && position < bottom-1){
            if(position > lowerControl.y ){
                adjustLowerControl(top,bottom,position+1)
                upperControl.y = position
            }else{
                upperControl.y = position
            }
        }
        upperLipHeight = (left.y - upperControl.y).toFloat()
    }


    fun adjustLowerControl(top : Int, bottom : Int, position : Int){
        if(position > top+1 && position < bottom-1){
            if(position < upperControl.y ){
                adjustUpperControl(top,bottom,position-1)
                lowerControl.y = position
            }else{
                lowerControl.y = position
            }
        }
        lowerLipHeight = (left.y - lowerControl.y).toFloat()
    }

    fun setCustomWidth(i : Int, max : Float){
        if( i < max/2){
            relativeWidth = ((max/2) - i).toInt()

        }else {
            relativeWidth = 0
        }
    }

    fun setPoints(a : Point, b : Point, bottom : Int, top : Int){
        left =  a
        right = b
        upperControl.x = a.x + (Math.abs(a.x-b.x)/2)
        upperControl.y = controlUpper(a,bottom,top)
        lowerControl.x = a.x + (Math.abs(a.x-b.x)/2)
        lowerControl.y = controlLower(a,bottom,top)
    }

    private fun controlUpper(point : Point, bottom : Int, top : Int) : Int{
        var up = point.y - upperLipHeight.toInt()
        if(up < top){
            up = top

        }else  if( up > bottom){
            up = bottom
        }
        return up
    }

    private fun controlLower(point : Point, bottom : Int, top : Int) : Int{
        var lower = point.y - lowerLipHeight.toInt()
        if(lower < top){
            lower = top

        }else  if( lower > bottom){
            lower = bottom
        }
        return lower
    }


}