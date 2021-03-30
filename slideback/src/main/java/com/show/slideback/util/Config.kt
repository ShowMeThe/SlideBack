package com.show.slideback.util

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

    var shadowWidth = 50

    var maxSideLength = 50f

    var slideEdgeYOff = 0.3f

    var slideSpeed = 0.5f

}