package com.show.slideback.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.FrameLayout
import com.show.slideback.SlideWatcher
import com.show.slideback.const.SlideConst.slideEdgeDistant
import com.show.slideback.const.SlideConst.slideEdgeRate
import com.show.slideback.const.SlideConst.slideEdgeYOff
import kotlin.math.abs

/**
 *  com.show.slideback.widget
 *  2020/12/26
 *  10:11
 *  ShowMeThe
 */
class SlideBackInterceptLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val maxSideLength = slideEdgeDistant
    private var downX = 0f
    private var allowToSlideBack = false
    private var slideLength = 0f

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return ev.action == MotionEvent.ACTION_DOWN && inRange(ev) && inRangeY(ev)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.action){
            MotionEvent.ACTION_DOWN ->{
                downX = event.rawX
                if(inRange(event)){
                    allowToSlideBack = true
                }
            }
            MotionEvent.ACTION_MOVE ->{
                if(allowToSlideBack){
                    val moveX = abs(event.rawX - downX)
                    slideLength = (moveX / slideEdgeRate).coerceAtMost(maxSideLength)
                }
            }
            MotionEvent.ACTION_UP ->{
                if(allowToSlideBack
                    && !inRange(event)
                    && inRangeY(event)
                    && slideLength >= maxSideLength){
                    onSliderBackListener?.invoke()
                }
                slideLength = 0f
                allowToSlideBack = false
            }
        }
        return true
    }

    private fun inRange(ev: MotionEvent) = (ev.rawX <= maxSideLength || ev.rawX >= width - maxSideLength)

    private fun inRangeY(ev: MotionEvent) = ev.rawY >= measuredHeight * slideEdgeYOff

    private var onSliderBackListener:(()->Unit)? = null
    fun setOnSliderBackListener(onSliderBackListener:(()->Unit)) {
        this.onSliderBackListener = onSliderBackListener
    }

}