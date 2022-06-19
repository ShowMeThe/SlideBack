package com.show.slideback.util

import android.util.TypedValue
import androidx.annotation.FloatRange

/**
 *  com.show.slideback.util
 *  2021/3/29
 *  22:25
 *  ShowMeThe
 */
class SlideConfig {

    companion object{
        private val instant by lazy { SlideConfig() }
        fun getConfig() = instant
    }

    var shadowWidth = 50

    var maxSlideX = 30f

    var slideOffsetY = 200f

    @FloatRange(from = 0.2,to = 1.0)
    var slideSpeed = 0.5f

}


