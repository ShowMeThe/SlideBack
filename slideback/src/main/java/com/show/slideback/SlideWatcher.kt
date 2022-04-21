package com.show.slideback

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.show.slideback.util.Utils
import com.show.slideback.widget.SlideBackInterceptLayout
import com.show.slideback.widget.SlideBackPreview
import java.lang.ref.WeakReference

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
        if (rootView != null) {
            decorView.removeViewAt(0)
            val interceptLayout = SlideBackInterceptLayout(SlideRegister.application)
            interceptLayout.setOnSliderBackListener {
                onSliderBackListener?.invoke()
            }
            if (!SlideRegister.register.activityPreviews.isNullOrEmpty()) {
                val act = SlideRegister.register.activityPreviews.last
                if (act != null && !act.activity.isFinishing) {
                    val color = Utils.getWindowBackgroundColor(act.activity)
                    val view = SlideBackPreview(SlideRegister.application)
                    view.weakWatch = WeakReference(act)
                    view.fadeBackGroundColor = color
                    view.visibility = View.INVISIBLE
                    interceptLayout.addView(
                        view, ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            }
            val fakeBackground = FrameLayout(SlideRegister.application)
                .apply {
                    setBackgroundColor(Utils.getWindowBackgroundColor(activity))
                }
            fakeBackground.addView(
                rootView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            interceptLayout.addView(
                fakeBackground, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            decorView.addView(interceptLayout)
        }
    }

    private var onSliderBackListener: (() -> Unit)? = null
    fun setOnSliderBackListener(onSliderBackListener: (() -> Unit)): SlideWatcher {
        this.onSliderBackListener = onSliderBackListener
        return this
    }

}