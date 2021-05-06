package com.show.slideback.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.customview.widget.ViewDragHelper
import com.show.slideback.util.Config
import kotlin.math.max

/**
 *  com.show.slideback.widget
 *  2020/12/26
 *  10:11
 *  ShowMeThe
 */
class SlideBackInterceptLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {


    private var orientation = Configuration.ORIENTATION_PORTRAIT
    private var previewChild: View? = null
    private var shadowView: View? = null
    private var contentChild: View? = null
    private var helper: ViewDragHelper? = null
    private var offsetX = 0
    var enableToSlideBack = true

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (childCount == 3 && previewChild == null) {
            initHelper()
            previewChild!!.translationX = (-measuredWidth * 0.5f)
            shadowView!!.translationX = -shadowView!!.measuredWidth.toFloat()
        }
    }

    private fun initHelper() {
        previewChild = getChildAt(0)
        shadowView = getChildAt(1)
        contentChild = getChildAt(2)
        helper = ViewDragHelper.create(this, 1f, object : ViewDragHelper.Callback() {

            override fun tryCaptureView(child: View, pointerId: Int): Boolean {
                return contentChild == child
            }

            override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
                offsetX = max(0, left)
                return offsetX
            }

            override fun onViewDragStateChanged(state: Int) {

            }

            override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
                return 0
            }


            override fun onEdgeDragStarted(edgeFlags: Int, pointerId: Int) {
                helper?.captureChildView(contentChild!!, pointerId)
            }

            override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
                if (offsetX > width / 2f) {
                    helper?.settleCapturedViewAt(width, 0)
                    onSliderBackListener?.invoke()
                } else {
                    helper?.settleCapturedViewAt(0, 0)
                    offsetX = 0
                }
                invalidate()
            }

            override fun onViewPositionChanged(
                changedView: View,
                left: Int,
                top: Int,
                dx: Int,
                dy: Int
            ) {
                previewChild!!.visibility = View.VISIBLE
                previewChild!!.translationX =
                    ((-measuredWidth + left) * Config.getConfig().slideSpeed).coerceAtMost(0f)
                shadowView!!.translationX = (-shadowView!!.measuredWidth.toFloat() + left)
            }

        })
        helper?.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT)
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return helper?.shouldInterceptTouchEvent(event) ?: super.onInterceptTouchEvent(event)
    }

    private fun inRange(ev: MotionEvent): Boolean {
        return ev.rawX <= measuredWidth * 0.05f
    }


    private var slideLegal = false
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && inRange(event) &&
            previewChild != null
            && (previewChild as SlideBackPreview).enableToSlideBack) {
            slideLegal = true
        }
        if (slideLegal) {
            helper?.processTouchEvent(event)
        }
        if (event.action == MotionEvent.ACTION_UP) {
            slideLegal = false
        }
        return true
    }


    override fun computeScroll() {
        helper?.apply {
            if (continueSettling(true)) {
                invalidate()
            }
        }
    }

    private var onSliderBackListener: (() -> Unit)? = null
    fun setOnSliderBackListener(onSliderBackListener: (() -> Unit)) {
        this.onSliderBackListener = onSliderBackListener
    }

}