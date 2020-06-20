package com

import android.app.Application
import android.graphics.PointF
import android.util.SizeF
import androidx.lifecycle.AndroidViewModel

class HeadViewModel(application: Application) : AndroidViewModel(application) {
    val head : HeadNew

    init{
        head = HeadNew(SizeF(46f,46f))
    }

    fun updateHeadPositions(topL : PointF?, topR : PointF?, bottomR : PointF?, bottomL : PointF?){
        head.updatePoints(topL,topR,bottomR,bottomL)
    }

    fun updateHeadCornerRadii(topL : Float?, topR : Float?, bottomR : Float?, bottomL : Float?){
        head.updateCornerRadii(topL,topR,bottomR,bottomL)
    }

    fun updateHeadColor(color : Int){
        head.color = color
    }

    fun invertHead(){
        head.invert()
    }

    fun selectMouth(selection : Int){
        head.selectFeature(selection)
    }




}