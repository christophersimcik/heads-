package com

import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.graphics.*
import android.os.Handler
import androidx.core.animation.addListener


class Eye(head : Head, testView: TestView) : BodyPart() {

    var active : Boolean = false

    var shape = OVAL
    val Head = head
    var centerPoint = PointF()
    var sizeHeight = 10f
    var sizeWidth = 10f
    var sizeH = 10f
    var sizeW = 10f
    var rotation = 0f
    lateinit var rotationPosition : PointF
    val testView = testView
    var scaleFactorH = 1f
    var scaleFactorW = 1f
    var myColor = Color.YELLOW
    var lidColor = Color.BLACK
    var eyelidPosition = 0f
    val blinker = Handler()
    lateinit var runBlink : Runnable
    lateinit var gradientColors : IntArray
    lateinit var gradientPositions : FloatArray
    val paint = Paint().apply { color = myColor }
    val rotationPaint = Paint().apply{color = Color.BLUE; alpha = 125}
    lateinit var blinkListener : Blink
    var span = 30.0f
    var heightOffset = 0.0f
    var heightRatio = 0.0f
    var leftPath = Path()
    var rightPath = Path()
    var equalateralize : Boolean = true
    var rotationMode : Boolean = false

    init{
        createBlinkResources()
        centerPoint = PointF(head.centerWidth,head.centerHeight)
        randomBlicking()
        setPath()

    }

    fun drawMe(canvas : Canvas){
        canvas.drawPath(leftPath,paint)
        canvas.drawPath(rightPath,paint)
        if(rotationMode){
            drawRotationDisplay(canvas)
        }
    }
    // create a triangular left path

    private fun createLeftTriangle(hor : Float, vert : Float) :Path {
        if(equalateralize) {
            val hor = findAdjacent(Math.max(sizeWidth,sizeHeight))
            val vert = findOpposite(Math.max(sizeWidth,sizeHeight))
        }
        val trianglePath = Path()
        val center = PointF(centerPoint.x - span, centerPoint.y - heightOffset)
        val pointA = calculatePoint(center.x, center.y, center.x - hor, center.y + vert, hor)
        val pointB = calculatePoint(center.x, center.y, center.x, center.y - vert, vert)
        val pointC = calculatePoint(center.x, center.y, center.x + hor, center.y + vert, hor)
        trianglePath.moveTo(pointA.x, pointA.y)
        trianglePath.lineTo(pointB.x, pointB.y)
        trianglePath.lineTo(pointC.x, pointC.y)
        trianglePath.lineTo(pointA.x,pointA.y)
        trianglePath.close()
        return trianglePath
    }

    //create a
    private fun createRightTriangle(hor : Float, vert : Float) : Path {
        if(equalateralize) {
            val hor = findAdjacent(sizeWidth)
            val vert = findOpposite(sizeWidth)
        }
        val trianglePath = Path()
        val center = PointF(centerPoint.x + span, centerPoint.y - heightOffset)
        val pointA = calculatePoint(center.x, center.y, center.x - (hor), center.y + vert, hor)
        val pointB = calculatePoint(center.x, center.y, center.x, center.y - vert, vert)
        val pointC = calculatePoint(center.x, center.y, center.x + (hor), center.y + vert, hor)
        trianglePath.moveTo(pointA.x, pointA.y)
        trianglePath.lineTo(pointB.x, pointB.y)
        trianglePath.lineTo(pointC.x, pointC.y)
        trianglePath.lineTo(pointA.x,pointA.y)
        trianglePath.close()
        return trianglePath
    }

    private fun findAdjacent(length : Float) : Float{
       return length * Math.cos(.60).toFloat()
    }
    private fun findOpposite(length : Float) : Float{
        return length * Math.sin(.60).toFloat()
    }

    private fun createLeftCircle() : Path {
        val diameter = Math.max(sizeWidth,sizeHeight)
        val circlePath = Path()
        val center = PointF(centerPoint.x - span, centerPoint.y - heightOffset)
        circlePath.addCircle(center.x,center.y,diameter/2, Path.Direction.CW)
        return circlePath
    }

    private fun createRightCircle() : Path {
        val diameter = Math.max(sizeWidth,sizeHeight)
        val circlePath = Path()
        val center = PointF(centerPoint.x + span, centerPoint.y - heightOffset)
        circlePath.addCircle(center.x,center.y,diameter/2, Path.Direction.CW)
        return circlePath
    }

    private fun createLeftRect(hor : Float, vert : Float) : Path {
        val rectPath = Path()
        val center = PointF(centerPoint.x - span, centerPoint.y - heightOffset)
        val pointA = PointF(center.x - (hor/2),center.y - (vert/2))
        val pointB = PointF(center.x + (hor/2),center.y - (vert/2))
        val pointC = PointF(center.x + (hor/2),center.y + (vert/2))
        val pointD = PointF(center.x - (hor/2),center.y + (vert/2))

        rectPath.moveTo(pointA.x,pointA.y)
        rectPath.lineTo(pointB.x,pointB.y)
        rectPath.lineTo(pointC.x,pointC.y)
        rectPath.lineTo(pointD.x,pointD.y)
        rectPath.close()

        return rectPath
    }

    private fun createRightRect(hor : Float, vert : Float) : Path {

        val rectPath = Path()
        val center = PointF(centerPoint.x + span, centerPoint.y - heightOffset)
        val pointA = PointF(center.x - (hor/2),center.y - (vert/2))
        val pointB = PointF(center.x + (hor/2),center.y - (vert/2))
        val pointC = PointF(center.x + (hor/2),center.y + (vert/2))
        val pointD = PointF(center.x - (hor/2),center.y + (vert/2))

        rectPath.moveTo(pointA.x,pointA.y)
        rectPath.lineTo(pointB.x,pointB.y)
        rectPath.lineTo(pointC.x,pointC.y)
        rectPath.lineTo(pointD.x,pointD.y)
        rectPath.close()

        return rectPath
    }

    fun rotatePaths(degrees : Float){
        val leftMatrix = Matrix()
        val rightMatrix = Matrix()
        var rect = RectF()
        leftPath.computeBounds(rect,false)
        leftMatrix.postRotate(degrees,rect.centerX(),rect.centerY())
        leftPath.transform(leftMatrix)
        rightPath.computeBounds(rect,false)
        rightMatrix.postRotate(degrees * -1,rect.centerX(),rect.centerY())
        rightPath.transform(rightMatrix)
    }

    fun scalePath(horz : Float, vert : Float){
        equalateralize = false
        scaleFactorW = sizeW * mapValues(horz)
        sizeWidth  =  scaleFactorW
        scaleFactorH = sizeH * mapValues(vert)
        sizeHeight = scaleFactorH
        setPath()
    }

    fun setSize(){
        sizeW = sizeWidth
        sizeH = sizeHeight
    }

    fun mapValues(value : Float) : Float{
        return (value / 500f)*(10f -.01f)+.01f
    }

    private fun calculatePoint(xA: Float, yA: Float, xB: Float, yB: Float, distance : Float): PointF {
        val point = PointF()
        val x = Math.pow((xB - xA).toDouble(), 2.0)
        val y = Math.pow((yB - yA).toDouble(), 2.0)
        val d = Math.sqrt(x + y).toFloat();
        var r  = distance / d
        point.x = r * xB + (1 - r) * xA
        point.y = r * yB + (1 - r) * yA
        return point
    }

    fun blink(){
        createBlinkResources()
        val eyelidDown = ValueAnimator.ofFloat(0.0f,1.0f).apply {
            duration = 500
            repeatCount = 1
            repeatMode = ValueAnimator.REVERSE
            addUpdateListener {
                    onProgress -> eyelidPosition = animatedValue as Float;paint.setShader(createBlinkResources()); blinkListener.onBlink()
            }

            addListener {
                object : AnimatorListenerAdapter() {}
            }
        }

        eyelidDown.start()
    }

    fun createBlinkResources():LinearGradient{
        lidColor = Head.color
        gradientColors = intArrayOf(lidColor,lidColor,myColor,myColor)
        gradientPositions = floatArrayOf(0.0f,eyelidPosition,eyelidPosition,1.0f)
        return LinearGradient(
                centerPoint.x, (centerPoint.y - heightOffset)-(sizeHeight),
                centerPoint.x, (centerPoint.y - heightOffset)+(sizeHeight),
                gradientColors,
                gradientPositions,
                Shader.TileMode.CLAMP
            )
    }

    interface Blink{
        fun onBlink()
    }


    fun registerBlinkListener(blink : Blink){
        blinkListener = blink
    }

    fun setPath(){
        when(shape){
            OVAL -> {leftPath = createLeftCircle(); rightPath = createRightCircle()}
            TRIANGLE -> {leftPath = createLeftTriangle(sizeWidth,sizeHeight); rightPath = createRightTriangle(sizeWidth,sizeHeight)}
            RECTANGLE -> {leftPath = createLeftRect(sizeWidth,sizeHeight); rightPath = createRightRect(sizeWidth,sizeHeight)}
        }
        rotatePaths(rotation)
    }

    fun drawRotationDisplay(canvas : Canvas){
        drawRotationPivot(canvas)
        drawRotationControl(canvas,findRotationPosition())
    }

    fun drawRotationPivot(canvas : Canvas){
        canvas.drawCircle(centerPoint.x,centerPoint.y,10f,rotationPaint)
    }

    fun drawRotationControl(canvas : Canvas, position : PointF){
        canvas.drawCircle(position.x,position.y,25f,rotationPaint)
    }

    fun findRotationPosition() : PointF{
        val radius = findRadius()
        val x =  centerPoint.x + Math.cos(rotation*Math.PI / 180f).toFloat() * radius
        val y =  centerPoint.y + Math.sin(rotation*Math.PI / 180f).toFloat() * radius
        rotationPosition = PointF(x,y)
        return rotationPosition
    }

    fun findRadius() : Float{
        return testView.width /4f
    }

    fun setRotation(point : PointF) {
      rotation = (Math.toDegrees(Math.atan2(centerPoint.y.toDouble() - point.y, centerPoint.x.toDouble() - point.x)).toFloat())-180f
    }

    fun cycleTypes(){
        if(shape < 2){
            shape ++
        }else{
            shape = 0
        }
    }

    fun randomBlicking(){
     runBlink = Runnable {     val interval = (Math.random()*5000)+1000;
         blink(); blinker.postDelayed(runBlink,interval.toLong())
     }
     blinker.postDelayed(runBlink,800)
    }

}