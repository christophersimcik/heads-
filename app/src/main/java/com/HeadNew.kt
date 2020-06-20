package com

import android.graphics.*
import android.util.SizeF

class HeadNew(size : SizeF){

    companion object {
        const val HEAD = 0
        const val MOUTH = 1
        const val EYE = 2
        const val NOSE = 3
    }

    lateinit var mPath : Path

    val mEye : Eye? = null
    val mMouth : Mouth by lazy {Mouth(this)}
    val mNose: Nose by lazy {Nose()}

    val sizeManager = SizeManager(size)
    val cornerManager = CornerManager()
    val pointManager = HeadManager()

    var color : Int = Color.BLACK

    val paint = Paint()

    var active : Boolean = true

    fun drawPath(canvas : Canvas) {
        paint.setColor(color)
        canvas.drawPath(mPath, paint)
    }

    fun drawPoints(canvas : Canvas){
        paint.setColor(pointManager.pointsMainColor)
        for(point in pointManager.mainPoints){
            canvas.drawCircle(point.x,point.y,pointManager.sizeMainPoints,paint)
        }
        paint.setColor(pointManager.pointsControlColor)
        for(point in pointManager.controlPoints){
            canvas.drawCircle(point.x,point.y,pointManager.sizeControlPoints,paint)
        }
    }

    fun updateCornerRadii(topL : Float?, topR : Float?, bottomR : Float?, bottomL : Float?){
        cornerManager.updatePercentages(topL,topR,bottomR,bottomL)
    }

    fun updatePoints(topL : PointF?, topR : PointF?, bottomR : PointF?, bottomL : PointF?){
        topL?.let {pointManager.topLeft = topL}
        topR?.let {pointManager.topRight = topR}
        bottomR?.let{pointManager.bottomRight = bottomR}
        bottomL?.let{pointManager.bottomLeft = bottomL}

        cornerManager.updateMaxValues(
            pointManager.calculateDistance(pointManager.topLeft, pointManager.topRight)/2,
            pointManager.calculateDistance(pointManager.bottomLeft, pointManager.bottomRight)/2,
            pointManager.calculateDistance(pointManager.bottomLeft, pointManager.topLeft)/2,
            pointManager.calculateDistance(pointManager.bottomRight, pointManager.topRight)/2
        )

        mPath = makePath()
    }

    private fun makePath() : Path {
        val headPath = Path()

        // side top
        pointManager.leftTop = calculatePoint(Head.SIDETOP, pointManager.mainBottomLeft, pointManager.mainTopLeft)
        headPath.moveTo(pointManager.leftTop.x, pointManager.leftTop.y)

        // topleft
        pointManager.topLeft = calculatePoint(Head.TOP, pointManager.mainTopRight, pointManager.mainTopLeft)
        headPath.quadTo( pointManager.mainTopLeft.x, pointManager.mainTopLeft.y, pointManager.topLeft.x, pointManager.topLeft.y)
        //define top left control point here

        // topright
        pointManager.topRight = calculatePoint(Head.TOP,pointManager.mainTopLeft,pointManager.mainTopRight)
        headPath.lineTo(pointManager.topRight.x,pointManager.topRight.y)

        // righttop
        pointManager.rightTop = calculatePoint(Head.SIDETOP,pointManager.bottomRight,pointManager.topRight)
        headPath.quadTo(pointManager.mainTopRight.x,pointManager.mainTopRight.y,pointManager.rightTop.x,pointManager.rightTop.y)
        //define top right control point here

        // rightbottom
        pointManager.rightBottom = calculatePoint(Head.SIDEBOTTOM,pointManager.mainTopRight,pointManager.mainBottomRight)
        headPath.lineTo(pointManager.rightBottom.x,pointManager.rightBottom.y)

        // bottomright
        pointManager.bottomRight = calculatePoint(Head.BOTTOM,pointManager.mainBottomLeft,pointManager.mainBottomRight)
        headPath.quadTo(pointManager.mainBottomRight.x,pointManager.mainBottomRight.y,pointManager.bottomRight.x,pointManager.bottomRight.y)
        //define bottom right control point here

        // bottomleft
        pointManager.bottomLeft = calculatePoint(Head.BOTTOM,pointManager.mainBottomRight,pointManager.mainBottomLeft)
        headPath.lineTo(pointManager.bottomLeft.x,pointManager.bottomLeft.y)

        // side bottom
        pointManager.leftBottom = calculatePoint(Head.SIDEBOTTOM,pointManager.mainTopLeft,pointManager.mainBottomLeft)
        headPath.quadTo(pointManager.mainBottomLeft.x,pointManager.mainBottomLeft.y,pointManager.leftBottom.x,pointManager.leftBottom.y)
        //define bottom left control point here

        // side top
        headPath.lineTo(pointManager.leftTop.x,pointManager.leftTop.y)

        headPath.close()

        return headPath
    }

    private fun calculatePoint(edge : Int, pointA : PointF, pointB : PointF): PointF {

        val newPoint = PointF()
        val x = Math.pow((pointB.x - pointA.x).toDouble(),2.0)
        val y = Math.pow((pointB.y - pointA.y).toDouble(),2.0)
        val d = Math.sqrt(x + y).toFloat();
        var r  = 0.0f

        when(edge){
            Head.TOP -> r = (d - (cornerManager.topMax * cornerManager.topPrcnt)) / d
            Head.BOTTOM ->  r = (d - (cornerManager.bottomMax * cornerManager.bottomPrcnt)) / d
            Head.SIDETOP -> r = (d - (cornerManager.topSideMax * cornerManager.topSidePrcnt)) / d
            Head.SIDEBOTTOM -> r = (d - (cornerManager.bottomSideMax * cornerManager.bottomSidePrcnt)) / d
        }
        newPoint.x = r * pointB.x + (1f - r) * pointA.x
        newPoint.y = r * pointB.y + (1f - r) * pointA.y
        return newPoint
    }

    fun invert(){
        pointManager.invert()
        cornerManager.invert()
        mPath = makePath()
    }

    fun selectFeature(selection : Int ){
        when(selection){
            HEAD -> {
                this.active = true
                mMouth.active = false
                mEye?.active = false
                mNose.active = false
            }
            MOUTH -> {
                this.active = false
                mMouth.active = true
                mEye?.active = false
                mNose.active = false
            }
            EYE -> {
                this.active = false
                mMouth.active = false
                mEye?.active = true
                mNose.active = false
            }
            NOSE -> {
                this.active = false
                mMouth.active = false
                mEye?.active = false
                mNose.active = true
            }
        }
    }

    fun getBounds() : RectF{
        val rectF = RectF()
        if(this::mPath.isInitialized){
            mPath.computeBounds(rectF,false)
        }
        return rectF
    }

}