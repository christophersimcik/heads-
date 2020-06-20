package com

import android.content.Context
import android.graphics.*
import android.os.Handler
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.myapplication.R

open class ColorSlider @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0

) : View(context, attrs, defStyleAttr) {

    val maxRGB = 255
    val paint = Paint()
    val markerPaint = Paint()
    lateinit var bar: RectF
    lateinit var linearGradient: LinearGradient
    var callbacks = arrayListOf<PositionCallback>()
    var position : Int = 0;
    var active : Boolean = false;

    init {
        markerPaint.color = resources.getColor(R.color.background_white,null)
        markerPaint.textSize = 0f
    }


    override fun onDraw(canvas: Canvas) {
        drawMe(canvas)
        if(active) {
            drawMarker(canvas)
            drawSizeText(canvas)
        }
        super.onDraw(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        bar = makeBar(w.toFloat(), h.toFloat() )
        setRed(0,0)
        setGreen(0,0)
        setBlue(0,0)
        markerPaint.textSize = calculateTextSize(resources.getString(R.string.text_sz_test))
        super.onSizeChanged(w, h, oldw, oldh)
    }

    fun makeBar(width: Float, height: Float): RectF {
        val rect = RectF()
        rect.set(0f, 0f, width, height)
        return rect;
    }

    fun setRed(blue: Int, green: Int) {
        val midHeight = bar.height() / 2
        val start = Color.rgb(0, green, blue)
        val end = Color.rgb(maxRGB, green, blue)
        linearGradient = LinearGradient(
            bar.left,
            midHeight,
            bar.right,
            midHeight,
            start,
            end,
            Shader.TileMode.CLAMP
        )
        invalidate()
    }

    fun setGreen(red : Int, blue: Int) {
        val midHeight = bar.height() / 2
        val start = Color.rgb(red,0, blue)
        val end = Color.rgb(red, maxRGB, blue)
        linearGradient = LinearGradient(
            bar.left,
            midHeight,
            bar.right,
            midHeight,
            start,
            end,
            Shader.TileMode.CLAMP
        )
        invalidate()
    }

    fun setBlue(red : Int, green : Int) {
        val midHeight = bar.height() / 2
        val start = Color.rgb(red,green,0)
        val end = Color.rgb(red, green, maxRGB)
        linearGradient = LinearGradient(
            bar.left,
            midHeight,
            bar.right,
            midHeight,
            start,
            end,
            Shader.TileMode.CLAMP
        )
        invalidate()
    }



    fun drawMe(canvas : Canvas){
        paint.setShader(linearGradient)
        canvas.drawRect(bar, paint)
    }

    fun drawMarker(canvas : Canvas){
        canvas.drawRect(createMarker(),markerPaint)
    }

    fun drawSizeText(canvas : Canvas){
        var heightAsFloat = height.toFloat();
        canvas.drawText(position.toString(),10f, heightAsFloat-heightAsFloat/4f,markerPaint)
    }

    fun calculatePosition(int : Int) : Int{
        var position = int * 255/width
        if(position < 0){position = 0}
        if(position > 255){position = 255}
       return  position
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action!!){
            MotionEvent.ACTION_DOWN -> active = true
            MotionEvent.ACTION_UP -> Handler().postDelayed(Runnable { active = false; invalidate() },750);
        }
        notifyCallbacks()
        return true
    }

    interface PositionCallback{
        fun updatePosition() {
        }
    }

    fun registerPositionCallback(callback : PositionCallback){
        callbacks.add(callback)
    }

    private fun notifyCallbacks() {
        for(i in callbacks){
            i.updatePosition()
        }
    }

    private fun createMarker() : RectF{
        val rect = RectF();
        val center = (position.toFloat()*(width.toFloat()/255f))
        rect.set(center-5f,0f,center+5f,height.toFloat())
        return rect;
    }

    private fun calculateTextSize(string : String) : Float{
        val rect = Rect()
        var sz = markerPaint.textSize
        markerPaint.getTextBounds(string,0,string.length,rect)
        while(rect.height() < getHeight()/2){
            sz ++
            markerPaint.textSize = sz
            markerPaint.getTextBounds(string,0,string.length,rect)
        }
        return sz
    }

}



