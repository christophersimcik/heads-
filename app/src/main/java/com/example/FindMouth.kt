package com.example

import android.graphics.Point
import com.Head

class FindMouth {
    val centerWidth = 0f
    val bottom = 0f
    val topLeft = Point()
    val topRight = Point()
    val bottomLeft = Point()
    val bottomRight = Point()

   fun findRect(points : Array<Point>) {
       val topL = points[0]
       val topR = points[1]
       val botL = points[2]
       val botR = points[3]

       val newTopL = Point()
       val newTopR = Point()
       val newBotL = Point()
       val newBotR = Point()

       val maxWidth = calculateDistance(botL,botR)*.75
       newBotL.x = (centerWidth - maxWidth).toInt()
       newBotL.y = botL.y + 25
       newBotR.x = (centerWidth + maxWidth).toInt()
       newBotR.y = botR.y + 25
       var tempPoint = findIntersect(maxWidth.toFloat(),botL,topL)
       newTopL.x = tempPoint.x +25
       newTopL.y = tempPoint.y
       tempPoint = findIntersect(maxWidth.toFloat(),botR,topR)
       newTopR.x = tempPoint.x -25
       newTopR.y = tempPoint.y
   }

    private fun findIntersect(targetDistance : Float, a: Point, b : Point) : Point{
        val distance = calculateDistance(a,b)
        var r  = 0f
        r = (distance - (distance-targetDistance) / distance)
        val x = r * b.x + (1 - r) * a.x
        val y = r * b.y + (1 - r) * b.y
        return Point(x.toInt(),y.toInt())
    }

    private fun calculateDistance(a : Point, b :Point) : Float{
        val x = Math.pow((b.x -a.x).toDouble(),2.0)
        val y = Math.pow((b.y -a.y).toDouble(),2.0)
        return Math.sqrt(x+y).toFloat()
    }

}