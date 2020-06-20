package com

import android.graphics.*

class Head() : BodyPart() {
    companion object{
        const val TOP = 0
        const val BOTTOM = 1
        const val SIDETOP = 2
        const val SIDEBOTTOM = 3
    }
    var centerWidth : Float = 0f
    var centerHeight : Float = 0f
    var rounded : Boolean = true
    var topRound= 0f;
    var bottomRound = 0f
    var topRoundMax = 0f
    var bottomRoundMax = 0f
    var sideRoundTop = 0f
    var sideRoundBottom = 0f
    var sideRoundMax = 0f
    var bottomPercentage = 0f
    var topPercentage = 0f
    var sideBotPercentage = 0f
    var sideTopPercentage = 0f
    var baseL = 0f
    var baseR = 0f
    var baseT = 0f
    var baseB = 0f
    var topLeft = 0f
    var topRight = 0f
    var bottomLeft = 0f
    var bottomRight = 0f
    var top = 0f
    var bottom = 0f
    var color = Color.BLACK
    var hardPoint = ArrayList<Point>()
    var controlPoint = ArrayList<Point>()
    val paint : Paint = Paint()
    val hardPaint = Paint()
    val cntrlPaint = Paint()

    fun drawPath(canvas : Canvas){
        hardPaint.color = Color.MAGENTA
        cntrlPaint.color = Color.BLUE
        paint.setColor(color)
        canvas.drawPath(generatePath(),paint)
        for(i in hardPoint){
            canvas.drawCircle(i.x.toFloat(),i.y.toFloat(),5f,hardPaint)
        }
        for(i in controlPoint){
            canvas.drawCircle(i.x.toFloat(),i.y.toFloat(),5f,cntrlPaint)
        }

    }

    fun generatePath() : Path{
        val path : Path = determinePath()
        return path
    }

    fun determinePath() : Path {
        if (rounded) {
            return makeRoundQuad()
        } else {
                return makeQuad()
        }
        return makeQuad()
    }

    private fun makeRoundQuad() : Path {
        var point : Array<Float>
        val quad = Path()
        hardPoint.clear()
        controlPoint.clear()

        // side top
        point = calculatePoint(SIDETOP,bottomLeft,bottom,topLeft,top)
        hardPoint.add(Point(point[0].toInt(),point[1].toInt()))
        quad.moveTo(point[0],point[1])
        // top
        point = calculatePoint(TOP,topRight,top,topLeft,top)
        hardPoint.add(Point(point[0].toInt(),point[1].toInt()))
        quad.quadTo(topLeft,top,point[0],point[1])
        controlPoint.add(Point(topLeft.toInt(),top.toInt()))
        // top
        point = calculatePoint(TOP,topLeft,top,topRight,top)
        hardPoint.add(Point(point[0].toInt(),point[1].toInt()))
        quad.lineTo(point[0],point[1])
        // side top
        point = calculatePoint(SIDETOP,bottomRight,bottom,topRight,top)
        hardPoint.add(Point(point[0].toInt(),point[1].toInt()))
        quad.quadTo(topRight,top,point[0],point[1])
        controlPoint.add(Point(topRight.toInt(),top.toInt()))
        // side bottom
        point = calculatePoint(SIDEBOTTOM,topRight,top,bottomRight,bottom)
        hardPoint.add(Point(point[0].toInt(),point[1].toInt()))
        quad.lineTo(point[0],point[1])
        // bottom
        point = calculatePoint(BOTTOM,bottomLeft,bottom,bottomRight,bottom)
        hardPoint.add(Point(point[0].toInt(),point[1].toInt()))
        quad.quadTo(bottomRight,bottom,point[0],point[1])
        controlPoint.add(Point(bottomRight.toInt(),bottom.toInt()))
        // bottom
        point = calculatePoint(BOTTOM,bottomRight,bottom,bottomLeft,bottom)
        hardPoint.add(Point(point[0].toInt(),point[1].toInt()))
        quad.lineTo(point[0],point[1])
        // side bottom
        point = calculatePoint(SIDEBOTTOM,topLeft,top,bottomLeft,bottom)
        hardPoint.add(Point(point[0].toInt(),point[1].toInt()))
        quad.quadTo(bottomLeft,bottom,point[0],point[1])
        controlPoint.add(Point(bottomLeft.toInt(),bottom.toInt()))
        // side top
        point = calculatePoint(SIDETOP,bottomLeft,bottom,topLeft,top)
        quad.lineTo(point[0],point[1])
        quad.close()
        return quad
    }

    fun scaleTop(scaleFactor : Float, incr : Float){
        topLeft  = baseL - (incr/10 * scaleFactor)
        topRight = baseR + (incr/10 * scaleFactor)
    }

    fun scaleBottom(scaleFactor : Float, incr : Float){
        bottomLeft  = baseL - (incr/10 * scaleFactor)
        bottomRight = baseR + (incr/10 * scaleFactor)
    }

    fun scaleHeight(scaleFactor : Float, incr : Float){
        top = baseT - (incr/10 * scaleFactor)
        bottom = baseB + (incr/10 *scaleFactor)
    }

    private fun calculatePoint(edge : Int, xA: Float, yA: Float, xB: Float, yB: Float): Array<Float> {
        val array = Array(2) { i -> 0f }
        val x = Math.pow((xB - xA).toDouble(),2.0)
        val y = Math.pow((yB - yA).toDouble(),2.0)
        val d = Math.sqrt(x + y).toFloat();
        var r  = 0f
        when(edge){
            TOP -> r = (d - (topRoundMax*topPercentage)) / d
            BOTTOM ->  r = (d - bottomRoundMax*bottomPercentage) / d
            SIDETOP -> r = (d - sideRoundMax*sideTopPercentage) / d
            SIDEBOTTOM -> r = (d - sideRoundMax*sideBotPercentage) / d
        }
        array[0] = r * xB + (1 - r) * xA
        array[1] = r * yB + (1 - r) * yA
        return array
    }

    private fun makeQuad() :Path{
        val quad = Path()
        hardPoint.clear()
        quad.moveTo(topLeft,top)
        quad.lineTo(topRight,top)
        hardPoint.add(Point(topRight.toInt(),top.toInt()))
        quad.lineTo(bottomRight,bottom)
        hardPoint.add(Point(bottomRight.toInt(),bottom.toInt()))
        quad.lineTo(bottomLeft,bottom)
        hardPoint.add(Point(bottomLeft.toInt(),bottom.toInt()))
        quad.lineTo(topLeft,top)
        hardPoint.add(Point(topLeft.toInt(),top.toInt()))
        quad.close()
        return quad;
    }

    fun initDim(){
        baseL = centerWidth
        baseR = centerWidth
        baseT = centerHeight
        baseB = centerHeight
    }

    fun updateTopPositions(x : Float, y : Float){
        top = baseT - y
        bottom = baseB + y
        topLeft = baseL - x
        topRight = baseR + x

        sideRoundMax = Math.abs(top-bottom)/2
        topRoundMax = Math.abs(topLeft-topRight)/2
        limitRound()
    }

    fun updateBottomPositions(x : Float, y : Float){
        top = baseT - y
        bottom = baseB + y
        bottomLeft = baseL - x
        bottomRight = baseR + x

        sideRoundMax = Math.abs(top-bottom)/2
        bottomRoundMax = Math.abs(bottomLeft-bottomRight)/2
        limitRound()
    }

    fun updatePositions(x : Float, y : Float){
        top = baseT - y
        bottom = baseB + y
        topLeft = baseL - x
        topRight = baseR + x
        bottomLeft = baseL - x
        bottomRight = baseR + x
        sideRoundMax = Math.abs(top-bottom)/2
        topRoundMax = Math.abs(topLeft-topRight)/2
        bottomRoundMax = Math.abs(bottomLeft-bottomRight)/2
        limitPrcnt()
        limitRound()
    }

    fun updateTopRound(float : Float){
            topRound = float
            topPercentage = topRound/topRoundMax
            sideRoundTop = topRound
            sideTopPercentage  = sideRoundTop/sideRoundMax
            limitPrcnt()
            limitRound()
    }

    fun updateBottomRound(float : Float){
            bottomRound = float
            bottomPercentage = bottomRound/bottomRoundMax
            sideRoundBottom = bottomRound
            sideBotPercentage = sideRoundBottom/sideRoundMax
            limitPrcnt()
            limitRound()
    }

    fun limitRound(){
        if(topRound>topRoundMax){topRound=topRoundMax*topPercentage}
        if(bottomRound>bottomRoundMax){bottomRound=bottomRoundMax*bottomPercentage}
        if(sideRoundTop>sideRoundMax){sideRoundTop=sideRoundMax*sideTopPercentage}
        if(sideRoundBottom>sideRoundMax){sideRoundBottom=sideRoundMax*sideBotPercentage}
    }
    fun limitPrcnt(){
        if(topPercentage > 1f){topPercentage = 1f}
        if(topPercentage < 0f){topPercentage = 0f}
        if(bottomPercentage > 1f){bottomPercentage = 1f}
        if(bottomPercentage < 0f){bottomPercentage = 0f}
        if(sideTopPercentage > 1f){sideTopPercentage = 1f}
        if(sideTopPercentage < 0f){sideTopPercentage = 0f}
        if(sideBotPercentage > 1f){sideBotPercentage = 1f}
        if(sideBotPercentage < 0f){sideBotPercentage = 0f}
    }

    fun invert(){
        // top left and bottom left
        val tempBotLeft = topLeft
        val tempBotRight = topRight
        topLeft = bottomLeft
        topRight = bottomRight
        bottomLeft = tempBotLeft
        bottomRight = tempBotRight
        // top right and bottom right
        val tempBotRnd = topRound
        val tempSideRndBottom = sideRoundTop
        topRound = bottomRound
        sideRoundTop = sideRoundBottom
        bottomRound = tempBotRnd
        sideRoundBottom = tempSideRndBottom

        val tempBotPrcnt = topPercentage
        val tempSideBotPrcnt = sideTopPercentage
        topPercentage = bottomPercentage
        sideTopPercentage = sideBotPercentage
        bottomPercentage = tempBotPrcnt
        sideBotPercentage = tempSideBotPrcnt
        //
        val tempBotRoundMax = topRoundMax
        topRoundMax = bottomRoundMax
        bottomRoundMax = tempBotRoundMax
        limitPrcnt()
        limitRound()

    }
}