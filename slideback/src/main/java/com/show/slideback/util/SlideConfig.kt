package com.show.slideback.util

import android.util.TypedValue
import androidx.annotation.FloatRange
import androidx.annotation.IntRange

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

    @FloatRange(from = 0.0,to = 15.0)
    var shadowWidth = 7f

    var maxSlideX = 30f

    var slideOffsetY = 200f

    var enableBlur = true

    @FloatRange(from = 0.0,to = 35.0)
    var blurRadius = 35f

    @FloatRange(from = 0.2,to = 1.0)
    var previewOffset = 0.5f

    @FloatRange(from = 0.1,to = 0.5)
    var slideSpeed = 0.15f
}


