package com.show.slideback

import android.app.Activity
import android.view.ViewGroup
import com.show.slideback.widget.SlideBackInterceptLayout
import kotlin.math.acos

/**
 *  com.show.slideback
 *  2020/12/26
 *  10:21
 *  ShowMeThe
 */
class SlideWatcher(val activity: Activity) {

    init {
        register()
    }

    private fun register() {
        val decorView = (activity.window.decorView as ViewGroup)
        val rootView = decorView.getChildAt(0)
        if(rootView!=null){
            decorView.removeViewAt(0)
            val interceptLayout = SlideBackInterceptLayout(activity)
            interceptLayout.setOnSliderBackListener {
                onSliderBackListener?.invoke()
            }
            interceptLayout.addView(rootView,ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
            decorView.addView(interceptLayout)
        }
    }

    private var onSliderBackListener:(()->Unit)? = null
    fun setOnSliderBackListener(onSliderBackListener:(()->Unit)):SlideWatcher{
        this.onSliderBackListener = onSliderBackListener
        return this
    }

}