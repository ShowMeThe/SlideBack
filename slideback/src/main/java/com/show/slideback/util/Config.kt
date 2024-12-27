package com.show.slideback.util

import androidx.annotation.FloatRange

/**
 *  com.show.slideback.util
 *  2021/3/29
 *  22:25
 *  ShowMeThe
 */
class Config {

    companion object{
        private val instant by lazy { Config() }
        fun getConfig() = instant
    }

    var shadowWidth = 25
    var maxSideLength = 75f
    var enableBlur = true
    @FloatRange(from = 0.0,to = 150.0)
    var blurRadius = 100f
    @FloatRange(from = 0.2,to = 1.0)
    var slideSpeed = 0.5f

}