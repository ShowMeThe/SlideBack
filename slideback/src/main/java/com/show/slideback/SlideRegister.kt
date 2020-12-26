package com.show.slideback

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.ArrayMap
import com.show.slideback.annotation.SlideBackBinder
import java.lang.ref.WeakReference
import java.util.*

/**
 *  com.show.slideback
 *  2020/12/26
 *  10:14
 *  ShowMeThe
 */
class SlideRegister private constructor(val application: Application) {

    companion object {
        private var register: SlideRegister? = null
        fun init(application: Application) {
            if (register == null) {
                synchronized(SlideRegister::class.java) {
                    if (register == null) {
                        register = SlideRegister(application)
                    }
                }
            }
        }
    }

    init {
        registerApplication()
    }

    private fun registerApplication() {
        application.registerActivityLifecycleCallbacks(object :
            Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                if(isActivityCanSlide(activity)){
                    activityWatcher[activity] = SlideWatcher(activity)
                        .setOnSliderBackListener{
                            activity.onBackPressed()
                        }
                }
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityResumed(activity: Activity) {
            }

            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityDestroyed(activity: Activity) {
                if(isActivityCanSlide(activity)){
                    activityWatcher.remove(activity)
                }
            }
        })
    }

    private val activityWatcher by lazy { ArrayMap<Activity,SlideWatcher>() }

    private fun isActivityCanSlide(activity: Activity) =
        activity::class.java.isAnnotationPresent(SlideBackBinder::class.java)

}