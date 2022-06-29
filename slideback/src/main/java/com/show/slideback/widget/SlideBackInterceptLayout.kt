package com.show.slideback.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import com.show.slideback.R
import com.show.slideback.util.SlideConfig
import java.util.logging.Logger
import kotlin.math.max
import kotlin.math.roundToInt

/**
 *  com.show.slideback.widget
 *  2020/12/26
 *  10:11
 *  ShowMeThe
 */
class SlideBackInterceptLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {


    private var hasTouch = false
    private var orientation = Configuration.ORIENTATION_PORTRAIT
    private var previewChild: View? = null
    private var contentChild: View? = null
    private var helper: ViewDragHelper? = null
    private var offsetX = 0
    var enableToSlideBack = true
    private val config by lazy { ViewConfiguration.get(getContext()) }
    private val mScaledMinimumFlingVelocity by lazy { config.scaledMinimumFlingVelocity }
    private val mScaledMaximumFlingVelocity by lazy { config.scaledMaximumFlingVelocity }
    private var mVelocityTracker: VelocityTracker? = null
    private var isFling = false
    private var isClosing = false
    private var slideLegal = false
    private val shadowSize = SlideConfig.getConfig().shadowWidth
    private val slideMaxWidth by lazy { dp2Px(SlideConfig.getConfig().maxSlideX) }
    private val sliderOffsetY by lazy { dp2Px(SlideConfig.getConfig().slideOffsetY) }
    private val mPaint by lazy {
        Paint().apply {
            isAntiAlias = true
        }
    }

    private val mLeftDrawable by lazy {
        ContextCompat.getDrawable(
            context,
            R.drawable.shadow_bottom
        )
    }

    private val mRect by lazy {
        Rect(0, 0, shadowSize, measuredHeight)
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (childCount == 2 && previewChild == null) {
            initHelper()
            previewChild?.translationX = (-measuredWidth * SlideConfig.getConfig().slideSpeed)
        }
    }

    private fun initHelper() {
        previewChild = getChildAt(0)
        contentChild = getChildAt(1)
        helper = ViewDragHelper.create(this, object : ViewDragHelper.Callback() {

            override fun tryCaptureView(child: View, pointerId: Int): Boolean {
                hasTouch = true
                return contentChild == child
            }

            override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
                offsetX = max(0, left)
                return offsetX
            }

            override fun onViewDragStateChanged(state: Int) {
                if (isClosing && state == ViewDragHelper.STATE_IDLE) {
                    onSliderBackListener?.invoke()
                }
            }

            override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
                return 0
            }


            override fun onEdgeDragStarted(edgeFlags: Int, pointerId: Int) {
                helper?.captureChildView(contentChild!!, pointerId)
            }

            override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
                when {
                    offsetX > width / 2f || isFling -> {
                        isClosing = true
                        helper?.smoothSlideViewTo(contentChild!!, width, 0)
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
                previewChild?.translationX =
                    ((-measuredWidth + left) * SlideConfig.getConfig().slideSpeed).coerceAtMost(0f)
                postInvalidate()
            }
        })
        helper?.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT)
    }


    override fun drawChild(canvas: Canvas, child: View?, drawingTime: Long): Boolean {
        val result = super.drawChild(canvas, child, drawingTime)
        if (child != null && child !is SlideBackPreview && hasTouch) {
            drawShadow(canvas, child)
        }
        return result
    }

    private fun drawShadow(canvas: Canvas, child: View) {
        val rect = mRect
        child.getHitRect(rect)
        mLeftDrawable?.alpha = ((1 - (child.left.toFloat() / measuredWidth)) * 255).roundToInt()
        mLeftDrawable?.setBounds(
            child.left - shadowSize, rect.top, rect.left, rect.bottom
        )
        mLeftDrawable?.draw(canvas)
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        val outYRange = outYRange(event)
        return if (outYRange) {
            (helper?.shouldInterceptTouchEvent(event) ?: super.onInterceptTouchEvent(
                event
            ) || inRange(
                event
            ))
        } else {
            super.onInterceptTouchEvent(event)
        }
    }

    private fun dp2Px(dp: Float) = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, dp,
        context.resources.displayMetrics
    )

    private fun inRange(ev: MotionEvent) = (ev.rawX <= slideMaxWidth)

    private fun outYRange(ev: MotionEvent) = (ev.rawY > sliderOffsetY)

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && inRange(event) && outYRange(event) &&
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
                isFling = velocityX >= (mScaledMaximumFlingVelocity * 0.25)
                mVelocityTracker?.clear()
                mVelocityTracker?.recycle()
                mVelocityTracker = null
            }
            helper?.processTouchEvent(event)
        }
        if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
            slideLegal = false
            helper?.processTouchEvent(event)
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
                ViewCompat.postInvalidateOnAnimation(this@SlideBackInterceptLayout)
            }
        }
    }

    private var onSliderBackListener: (() -> Unit)? = null
    fun setOnSliderBackListener(onSliderBackListener: (() -> Unit)) {
        this.onSliderBackListener = onSliderBackListener
    }


}