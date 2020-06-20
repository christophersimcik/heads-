package com

import android.content.Context
import android.graphics.Canvas
import android.graphics.Point
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import kotlin.math.round

class NEW @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    val head by lazy { Head() }
    private var scaleFactorTop = 1.0f
    private var scaleFactorBottom = 1.0f
    private var scaleFactorHeight = 1.0f
    private val scaleListener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {

            if (detector.currentSpanX > detector.currentSpanY) {

                if (Math.abs(detector.focusY - head.top) < 100) {
                    scaleFactorTop *= detector.scaleFactor
                    scaleFactorTop =
                        Math.max(0.1f, Math.min(scaleFactorTop, ((width / 10) / 2).toFloat()))
                } else if (Math.abs(detector.focusY - head.bottom) < 100) {
                    scaleFactorBottom *= detector.scaleFactor
                    scaleFactorBottom =
                        Math.max(0.1f, Math.min(scaleFactorBottom, ((width / 10) / 2).toFloat()))
                } else {
                    if (detector.focusY > head.top && detector.focusY < head.bottom) {
                        // scale both
                        scaleFactorTop *= detector.scaleFactor
                        scaleFactorTop =
                            Math.max(0.1f, Math.min(scaleFactorTop, ((width / 10) / 2).toFloat()))
                        // scale both
                        scaleFactorBottom *= detector.scaleFactor
                        scaleFactorBottom = Math.max(
                            0.1f,
                            Math.min(scaleFactorBottom, ((width / 10) / 2).toFloat())
                        )
                    }
                }

            } else {
                scaleFactorHeight *= detector.scaleFactor
                scaleFactorHeight =
                    Math.max(0.1f, Math.min(scaleFactorHeight, ((height / 10) / 2).toFloat()))
            }
            invalidate()
            return true
        }
    }
    private val scaleDetector = ScaleGestureDetector(getContext(), scaleListener)

    override fun onDraw(canvas: Canvas?) {
        head.drawPath(canvas!!)
        head.scaleTop(scaleFactorTop, width.toFloat())
        head.scaleHeight(scaleFactorHeight, width.toFloat())
        head.scaleBottom(scaleFactorBottom, width.toFloat())
        super.onDraw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleDetector.onTouchEvent(event)
        return true

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        head.centerWidth = (w / 2).toFloat()
        head.centerHeight = (h / 2).toFloat()
        head.initDim()
        super.onSizeChanged(w, h, oldw, oldh)
    }


}

/*
   val currentPosition = Point(event.getX().toInt(), event.getY().toInt())
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                roundingControl.start(currentPosition)
                System.out.print("view down")
            }
            MotionEvent.ACTION_UP -> {
                roundingControl.stop()
                System.out.print("view up")
            }
            MotionEvent.ACTION_MOVE -> {
                roundingControl.update(currentPosition)
                System.out.print("view move")
            }
        }
 */