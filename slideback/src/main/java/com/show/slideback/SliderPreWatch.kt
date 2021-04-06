package com.show.slideback

import android.app.Activity
import android.util.Log
import android.view.View
import com.show.slideback.util.Utils

class SliderPreWatch(val activity: Activity) {

    var contentView: View? = null
        set(value) {
            field = value
            onUpdate?.invoke()
        }

    init {
        contentView = Utils.getContentView(activity)
    }

    private var onUpdate: (() -> Unit)? = null
    fun onUpdate(onUpdate: (() -> Unit)?) {
        this.onUpdate = onUpdate
    }


    override fun equals(other: Any?): Boolean {
        return activity == (other as SliderPreWatch?)?.activity
    }

    override fun hashCode(): Int {
        return activity.hashCode()
    }
}