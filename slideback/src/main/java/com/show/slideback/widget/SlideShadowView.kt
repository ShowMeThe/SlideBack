package com.show.slideback.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.FloatRange


/**
 *  com.show.slideback.widget
 *  2021/3/29
 *  22:58
 *  ShowMeThe
 */
class SlideShadowView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

   private val colors = intArrayOf(
       Color.parseColor("#00000000"), Color.parseColor("#0A000000"), Color.parseColor("#A1000000")
    )
    private val mLinearGradient by lazy { LinearGradient(0f, 0f, measuredWidth.toFloat(), 0f, colors, null, Shader.TileMode.REPEAT) }
    private val mPaint by lazy {
        Paint().apply {
            isAntiAlias = true
        }
    }
    private val mRectF by lazy { RectF(0f,0f,measuredWidth.toFloat(),measuredHeight.toFloat()) }

    override fun onDraw(canvas: Canvas) {
        mPaint.shader = mLinearGradient
        canvas.drawRect(mRectF, mPaint)

    }

}