package com.show.slideback

import android.app.Application
import android.content.Context
import com.show.launch.Initializer
import kotlinx.coroutines.CancellableContinuation

/**
 *  com.show.slideback
 *  2020/12/26
 *  10:01
 *  ShowMeThe
 */
class SlideInitializer : Initializer<Boolean>{
    override fun onCreate(
        context: Context,
        isMainProcess: Boolean,
        continuation: CancellableContinuation<Boolean>?
    ) {
        SlideRegister.init(context.applicationContext as Application)
    }
}