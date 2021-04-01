package com.show.slideback.widget

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.show.slideback.util.Utils
import java.lang.ref.WeakReference

/**
 *  com.show.slideback.widget
 *  2021/3/29
 *  21:53
 *  ShowMeThe
 */
class SlideBackPreview @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint()
        .apply {
            isAntiAlias = true
        }
    var weakAct : WeakReference<Activity>? = null
    set(value) {
        field = value
        if(weakAct?.get() != null && !weakAct?.get()!!.isFinishing){
            contentView = WeakReference(Utils.getContentView(weakAct?.get()!!))
        }
    }
    private var contentView: WeakReference<View>? = null
        set(value) {
            field = value
            if (field != null) {
                postInvalidate()
            }
        }
    var fadeBackGroundColor = Color.WHITE
        set(value) {
            field = value
            paint.color = field
        }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), paint)
        contentView?.get()?.draw(canvas)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        contentView = null
        weakAct = null
    }

}