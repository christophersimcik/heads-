package com

class CornerManager {

    // max values based on measuremnts
    var topMax = 0.0f
    var bottomMax = 0.0f
    var topSideMax = 0.0f
    var bottomSideMax = 0.0f

    // percentage of Max
    var topPrcnt = 0.0f
    var bottomPrcnt = 0.0f
    var topSidePrcnt = 0.0f
    var bottomSidePrcnt = 0.0f

    // values
    private var top = 0.0f
    private var bottom = 0.0f
    private var topSide = 0.0f
    private var bottomSide = 0.0f

    fun invert(){
        var temp : Float
        temp = topPrcnt
        topPrcnt = bottomPrcnt
        bottomPrcnt = temp
        temp = topSidePrcnt
        topSidePrcnt = bottomSidePrcnt
        bottomSidePrcnt = temp
    }

    fun updateMaxValues(top : Float?, bottom : Float?, topS : Float?, bottomS : Float?){
        top?.let{this.topMax = top}
        bottom?.let{this.bottomMax = bottom}
        topS?.let{this.topSideMax = topS}
        bottomS?.let{this.bottomSideMax = bottomS}
        updateValues()
    }

    fun updatePercentages(top : Float?, bottom : Float?, topS : Float?, bottomS : Float?){
        top?.let{if(top <= 1f)this.topPrcnt = top}
        bottom?.let{if(bottom <= 1f)this.bottomPrcnt = bottom}
        topS?.let{if(topS <= 1f)this.topSidePrcnt = topS}
        bottomS?.let{if(bottomS <= 1f)this.bottomSidePrcnt = bottomS}
        updateValues()
    }

    private fun updateValues() {
        top = topMax * topPrcnt
        bottom = bottomMax * bottomPrcnt
        topSide = topSideMax * topSidePrcnt
        bottomSide = bottomSideMax * bottomSidePrcnt
    }

    fun getTop(): Float {
        return top
    }

    fun getBottom(): Float {
        return bottom
    }

    fun getTopSide(): Float {
        return topSide
    }

    fun getBottomSide(): Float {
        return bottomSide
    }
}