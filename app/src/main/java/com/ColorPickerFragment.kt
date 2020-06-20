package com

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.R

class ColorPickerFragment : Fragment(), ColorSlider.PositionCallback {

    lateinit var red : ColorSlider
    lateinit var green : ColorSlider
    lateinit var blue : ColorSlider
    lateinit var colorPreview : View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.test_color,container)
        val fragmentManager = activity?.supportFragmentManager
        view.viewTreeObserver.addOnGlobalLayoutListener { updateAll(); view.viewTreeObserver.removeOnGlobalLayoutListener { this } }
        colorPreview = view.findViewById(R.id.color_preview)
        red = view.findViewById(R.id.red)
        red.registerPositionCallback(this)
        red.setOnTouchListener{ view : View, motionEvent : MotionEvent ->
            red.position = red.calculatePosition(motionEvent.getX().toInt()); false}
        green = view.findViewById(R.id.green)
        green.registerPositionCallback(this)
        green.setOnTouchListener{ view : View, motionEvent : MotionEvent ->
            green.position = green.calculatePosition(motionEvent.getX().toInt()); false}
        blue = view.findViewById(R.id.blue)
        blue.registerPositionCallback(this)
        blue.setOnTouchListener{ view : View, motionEvent : MotionEvent ->
            blue.position = blue.calculatePosition(motionEvent.getX().toInt()); false}
        return view
    }

    override fun updatePosition() {updateAll()}

    private fun updateAll(){
        red.setRed(green.position,blue.position)
        green.setGreen(red.position,blue.position)
        blue.setBlue(red.position,green.position);
        view?.setBackgroundColor(Color.rgb(red.position,green.position,blue.position))
    }
}