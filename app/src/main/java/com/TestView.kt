package com

import android.content.Context
import android.graphics.*
import android.os.Handler
import android.provider.CalendarContract
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.Button
import com.example.myapplication.R
import kotlinx.coroutines.Runnable
import kotlin.math.round

class TestView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr), Eye.Blink {

    companion object{
        const val HEAD = 0
        const val MOUTH = 1
        const val NOSE = 2
        const val EYES = 3
    }

    val head = Head()
    val mouth = Mouth()

    // check lazy init
    lateinit var currentPosition : Point
    lateinit var initialPosition : Point
    val eye = Eye(head, this)
    private var centerWidth = 0f
    private var centerHeight = 0f
    private var location = 0
    private var clicks = 0;
    lateinit private var clickedPoint : Point
    private var doubleClick : Boolean = false
    private var timingClicks : Boolean = false
    private val mHandler = Handler()
    private var scaleFactor = 1.0f
    private var up = false
    private var down = false
    private var select = HEAD

    init {
        eye.registerBlinkListener(this)
    }

    override fun onDraw(canvas: Canvas) {
        head.drawPath(canvas)
        if(doubleClick && select == HEAD){
            canvas.drawCircle(clickedPoint.x.toFloat(),clickedPoint.y.toFloat(),50f, Paint().apply{color = Color.RED;alpha = 100})
        }
        if(doubleClick && select == MOUTH){
            mouth.drawLipControlPoints(canvas)
        }
        mouth.drawPath(canvas)
        eye.centerPoint = PointF(centerWidth,centerHeight)
        eye.setPath()
        eye.drawMe(canvas)
        super.onDraw(canvas)
    }

    private val scaleListener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleFactor *= detector.scaleFactor
            Math.max(1f, Math.min(scaleFactor,(10).toFloat()))
            when(select){
                HEAD ->{
                    when(location){
                        1 -> {head.updateTopRound(head.topRoundMax*scaleFactor)}
                        2 -> {head.updateBottomRound(head.bottomRoundMax*scaleFactor)}
                    }
                }
            }

            invalidate()
            return true
        }
    }
    private val scaleDetector = ScaleGestureDetector(getContext(), scaleListener)

    fun calculateDistance(pointA : Point, pointB : Point) : Float{
        val a = Math.pow((pointB.x - pointA.x).toDouble(),2.0)
        val b = Math.pow((pointB.y - pointA.y).toDouble(),2.0)
        return Math.sqrt(a+b).toFloat()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        invalidate()
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        initialPosition = Point(event.x.toInt(),event.y.toInt())
                        checkInversion(event.y)
                        lookForDoubleClick()
                        determineQuadrant(initialPosition)
                    }

                    MotionEvent.ACTION_UP -> {
                        when(select){
                            EYES ->{
                                if(doubleClick){
                                    eye.setSize()
                                }
                            }
                        }
                        determineDoubleClick()
                    }

                    MotionEvent.ACTION_MOVE -> {
                        currentPosition = Point(event.x.toInt(),event.y.toInt())
                        when(select){
                            EYES ->{
                                if(doubleClick){
                                    if(eye.rotationMode){
                                        eye.setRotation(PointF(currentPosition.x.toFloat(),currentPosition.y.toFloat()))
                                    }else{
                                    val horz = Math.abs(currentPosition.x - initialPosition.x).toFloat()
                                    var vert = Math.abs(currentPosition.y - initialPosition.y).toFloat()
                                    System.out.println("first = " + initialPosition + " point = " + currentPosition)
                                    eye.scalePath(horz, vert)
                                    }
                                } else {
                                    eye.heightOffset = (centerHeight - currentPosition.y)
                                    eye.span = (centerWidth - currentPosition.x)
                                    val total = Math.abs(centerHeight - head.top)
                                    val eyeHeight = centerHeight - currentPosition.y
                                    eye.heightRatio = eyeHeight/total

                                }
                            }
                            HEAD -> {
                                reshapeHead(currentPosition); getMouthPoints(Math.abs(head.bottom-head.top)* mouth.relativeHeight)
                                getEyeHeight()
                            }
                            MOUTH -> {
                                if (doubleClick) {

                                    if(calculateDistance(currentPosition,mouth.upperControl) <  calculateDistance(currentPosition,mouth.lowerControl)){
                                        mouth.adjustUpperControl(top,bottom,currentPosition.y)
                                    }else{
                                        mouth.adjustLowerControl(top,bottom,currentPosition.y)
                                    }

                                } else {
                                    setMouthRelativeHeight(head.top, head.bottom,currentPosition.y.toFloat())
                                    setMouthRelativeWidth(
                                        currentPosition.x,
                                        Math.abs(head.bottom - head.top) * mouth.relativeHeight
                                    )
                                    getMouthPoints(Math.abs(head.bottom - head.top) * mouth.relativeHeight)
                                }
                            }

                        }
                        invalidate()


                    }
                }
        if(doubleClick) {
            when(select){
                HEAD -> {scaleDetector.onTouchEvent(event)}
            }
        }
            return true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        centerWidth = (w / 2).toFloat()
        centerHeight = (h / 2).toFloat()
        head.centerWidth = centerWidth
        head.centerHeight = centerHeight
        head.initDim()
        super.onSizeChanged(w, h, oldw, oldh)
    }

    private fun getMouthPoints(dist : Float ){
        val leftBottom = Point(head.bottomLeft.toInt(),head.bottom.toInt())
        val leftTop = Point(head.topLeft.toInt(),head.top.toInt())
        val rightBottom =  Point(head.bottomRight.toInt(),head.bottom.toInt())
        val rightTop = Point(head.topRight.toInt(),head.top.toInt())
        val leftPoint = findIntersect(dist,leftTop,leftBottom)
        leftPoint.x += mouth.relativeWidth
        val rightPoint = findIntersect(dist,rightTop,rightBottom)
        rightPoint.x -= mouth.relativeWidth
        mouth.setPoints(leftPoint,rightPoint,head.bottom.toInt(),head.top.toInt())
    }

    private fun maxMouth(dist : Float ) : Float{
        val leftBottom = Point(head.bottomLeft.toInt(),head.bottom.toInt())
        val leftTop = Point(head.topLeft.toInt(),head.top.toInt())
        val rightBottom =  Point(head.bottomRight.toInt(),head.bottom.toInt())
        val rightTop = Point(head.topRight.toInt(),head.top.toInt())
        val leftPoint = findIntersect(dist,leftTop,leftBottom)
        val rightPoint = findIntersect(dist,rightTop,rightBottom)
        return calculateDistance(leftPoint,rightPoint)
    }

    private fun setMouthRelativeHeight(top : Float, bottom : Float, position : Float){
        val maxHeight = Math.abs(bottom - top)
        val currentHeight= Math.abs(bottom - position)
        var check = currentHeight / maxHeight
        if(check > 1f){
            check = 1f
        }
        if(check < 0f){
            check = 0f
        }
        mouth.relativeHeight = check
    }

    private fun setMouthRelativeWidth(position : Int, distance : Float){
       val maxDistance = maxMouth(distance)
       mouth.setCustomWidth(Math.abs(centerWidth-position).toInt(), maxDistance)
    }

    private fun findIntersect(targetDistance : Float, a: Point, b : Point) : Point{
        val distance = calculateDistance(b,a)
        val r = (distance-targetDistance) / distance
        val x = (r * b.x + (1 - r) * a.x)
        val y = (r * b.y + (1 - r) * a.y)
        val point = Point(x.toInt(),y.toInt())
        return point
    }

    private fun checkInversion(y : Float){
        if(y < centerHeight){
            up = true
            down = false
        }else{
            up= false
            down = true
        }
    }

    private fun lookForDoubleClick(){
        if (!timingClicks) {
            timingClicks = true;
            mHandler.postDelayed(Runnable { clicks = 0; timingClicks = false }, 250)
        }
    }

    private fun determineDoubleClick(){
        if (timingClicks) {
            clicks++
            if (clicks > 1) {
                if(doubleClick){
                    doubleClick = false
                }else{
                    doubleClick = true
                }
                this.invalidate()
            }
        }
    }

    private fun determineQuadrant(point : Point) {
        var corner: Point

        // check top left
        corner = Point(head.topLeft.toInt(), head.top.toInt())
        if (calculateDistance(point, corner) < 100) {
            location = 1
            if (!doubleClick) {
                clickedPoint = Point(head.topLeft.toInt(), head.top.toInt())
            }
        }
        // check top right
        corner = Point(head.topRight.toInt(), head.top.toInt())
        if (calculateDistance(point, corner) < 100) {
            location = 1
            if (!doubleClick) {
                clickedPoint = Point(head.topRight.toInt(), head.top.toInt())
            }
        }
        //check bottom left
        corner = Point(head.bottomLeft.toInt(), head.bottom.toInt())
        if (calculateDistance(point, corner) < 100) {
            location = 2
            if (!doubleClick) {
                clickedPoint = Point(head.bottomLeft.toInt(), head.bottom.toInt())
            }
        }
        //check bottom right
        corner = Point(head.bottomRight.toInt(), head.bottom.toInt())
        if (calculateDistance(point,corner) < 100){
            location = 2
            if (!doubleClick) {
                clickedPoint = Point(head.bottomRight.toInt(), head.bottom.toInt())
            }
        }
    }
    // reshape the head
    private fun reshapeHead(point : Point){
        if (!doubleClick) {
            val x = Math.abs(centerWidth - point.x)
            val y = Math.abs(centerHeight - point.y)
            if (down && point.y < centerHeight) {
                head.invert()
                down = false
                up = true
            }
            if (up && point.y > centerHeight) {
                head.invert()
                up = false
                down = true
            }
            if (point.y > centerHeight) {
                head.updateBottomPositions(x, y)
            } else {
                head.updateTopPositions(x, y)
            }
            //invalidate()
        }
    }

    // eyes

    private fun getEyeHeight(){
        val total = Math.abs(head.top-centerHeight)
        eye.heightOffset = total * eye.heightRatio
        System.out.println("***" + " height offset = " + eye.heightOffset)
        eye.setPath()

    }





    private fun getLocation(event : MotionEvent) : Point{
        return Point(event.x.toInt(),event.y.toInt())
    }


    fun select(bodypart : Int){
        select = bodypart
    }

    override fun onBlink() {
        System.out.println("invalidated")
        invalidate()
    }

    fun rotationToggle(button : Button){
        if(eye.rotationMode){
            eye.rotationMode = false
            button.setBackgroundColor(resources.getColor(R.color.design_default_color_primary,null))
        }else{
            eye.rotationMode = true
            button.setBackgroundColor(resources.getColor(R.color.colorAccent,null))
        }
    }

    fun typeToggle(button : Button){
        eye.cycleTypes()
        eye.setPath()
        button.setBackgroundColor(resources.getColor(R.color.colorAccent,null))
        Handler().postDelayed(Runnable{button.setBackgroundColor(resources.getColor(R.color.colorPrimary,null))},250)
    }
}
