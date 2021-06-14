package com.show.slideback.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.widget.FrameLayout
import androidx.customview.widget.ViewDragHelper
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
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
    private val config by lazy { ViewConfiguration.get(getContext()) }
    private val mScaledMinimumFlingVelocity by lazy { config.scaledMinimumFlingVelocity }
    private val mScaledMaximumFlingVelocity by lazy { config.scaledMaximumFlingVelocity }
    private var mVelocityTracker: VelocityTracker? = null
    private var isFling = false
    private val interpolator = FastOutLinearInInterpolator()
    private val duration = 300L


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (childCount == 3 && previewChild == null) {
            initHelper()
            previewChild?.translationX = (-measuredWidth * Config.getConfig().slideSpeed)
            shadowView?.translationX = -shadowView!!.measuredWidth.toFloat()
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
                when {
                    isFling -> {
                        tryToDoAnimator()
                    }
                    offsetX > width / 2f -> {
                        helper?.settleCapturedViewAt(width, 0)
                        onSliderBackListener?.invoke()
                    }
                    else -> {
                        helper?.settleCapturedViewAt(0, 0)
                        offsetX = 0
                    }
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
                previewChild?.visibility = View.VISIBLE
                previewChild?.visibility = View.VISIBLE
                previewChild?.translationX = ((-measuredWidth + left) * Config.getConfig().slideSpeed).coerceAtMost(0f)

                shadowView?.translationX = (-shadowView!!.measuredWidth.toFloat() + left)
            }

        })
        helper?.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT)
    }


    private fun tryToDoAnimator() {
        val layoutWidth = width.toFloat()
        contentChild?.apply {
            translationX = offsetX.toFloat()
            val ptx = previewChild!!.translationX
            animate()
                .withEndAction {
                    onSliderBackListener?.invoke()
                }
                .setUpdateListener {
                    val value = it.animatedValue as Float
                    previewChild?.translationX = ptx * (1 - value)
                }
                .translationX(layoutWidth)
                .setInterpolator(interpolator)
                .setDuration(duration)
                .start()
        }
        shadowView?.apply {
             visibility = View.GONE
        }
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return helper?.shouldInterceptTouchEvent(event) ?: super.onInterceptTouchEvent(event)
    }

    private fun inRange(ev: MotionEvent) = (ev.rawX <= Config.getConfig().maxSideLength)


    private var slideLegal = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && inRange(event) &&
            previewChild != null && (previewChild as SlideBackPreview).enableToSlideBack
        ) {
            slideLegal = true
        }
        initVelocityTrackerIfNull()
        mVelocityTracker?.addMovement(event)

        if (slideLegal) {
            if ((event.action == MotionEvent.ACTION_UP
                        || event.action == MotionEvent.ACTION_CANCEL)
                && enableToSlideBack && helper != null
            ) {
                mVelocityTracker?.computeCurrentVelocity(
                    1000,
                    mScaledMaximumFlingVelocity.toFloat()
                )
                val velocityX = mVelocityTracker?.xVelocity ?: mScaledMinimumFlingVelocity.toFloat()
                isFling = velocityX >= (mScaledMaximumFlingVelocity * 0.4)
                mVelocityTracker?.clear()
                mVelocityTracker?.recycle()
                mVelocityTracker = null
            }
            helper?.processTouchEvent(event)
        }
        if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
            slideLegal = false
        }
        return true
    }

    private fun initVelocityTrackerIfNull() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }
    }

    override fun computeScroll() {
        helper?.apply {
            if (continueSettling(true)) {
                postInvalidate()
            }
        }
    }

    private var onSliderBackListener: (() -> Unit)? = null
    fun setOnSliderBackListener(onSliderBackListener: (() -> Unit)) {
        this.onSliderBackListener = onSliderBackListener
    }

}