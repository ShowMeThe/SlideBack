package com.show.slideback.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.util.AttributeSet
import android.view.View
import com.show.slideback.SliderPreWatch
import com.show.slideback.util.SlideConfig
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


    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && SlideConfig.getConfig().enableBlur) {
            val radius = SlideConfig.getConfig().blurRadius
            setRenderEffect(RenderEffect.createBlurEffect(radius,radius, Shader.TileMode.CLAMP))
        }
    }

    private val paint = Paint()
        .apply {
            isAntiAlias = true
        }
    var enableToSlideBack = true


    var contentView: WeakReference<View>? = null
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

    var weakWatch: WeakReference<SliderPreWatch>? = null
        set(value) {
            field = value
            field?.get()?.also {
                this.contentView = WeakReference(it.contentView)
                it.onUpdate{
                    this.contentView = WeakReference(it.contentView)
                }
            }
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), paint)
        contentView?.get()?.draw(canvas)
    }

    override fun onDetachedFromWindow() {
        onActDestroy()
        super.onDetachedFromWindow()
    }


    private fun onActDestroy() {
        weakWatch?.get()?.onUpdate(null)
        weakWatch = null
        contentView = null
    }

}