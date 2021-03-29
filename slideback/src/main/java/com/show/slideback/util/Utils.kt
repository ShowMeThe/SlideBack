package com.show.slideback.util

import android.app.Activity
import android.graphics.Color
import android.util.Log
import android.view.ViewGroup

/**
 *  com.show.slideback.util
 *  2021/3/29
 *  22:01
 *  ShowMeThe
 */
object Utils {

    fun getContentView(activity: Activity) = (activity.window.decorView as ViewGroup).getChildAt(0)

    fun getWindowBackgroundColor(activity: Activity) = activity.let {
        val theme = it.theme
        val ids = intArrayOf(android.R.attr.windowBackground)
        val array = theme.obtainStyledAttributes(ids)
        val color = array.getColor(0, Color.WHITE)
        array.recycle()
        color
    }

}