package com.show.slideback

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.ArrayMap
import com.show.slideback.annotation.SlideBackBinder
import com.show.slideback.annotation.SlideBackPreview
import java.lang.ref.WeakReference
import java.util.*

/**
 *  com.show.slideback
 *  2020/12/26
 *  10:14
 *  ShowMeThe
 */
class SlideRegister  {

    companion object {
        private lateinit var application:Application
        val register: SlideRegister by lazy { SlideRegister() }
        fun init(application: Application) {
            this.application = application
            register.registerApplication()
        }
    }

    private fun registerApplication() {
        application.registerActivityLifecycleCallbacks(object :
            Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                if(isActivityCanSlide(activity)){
                    activityWatcher[activity] = SlideWatcher(activity)
                        .setOnSliderBackListener{
                            activity.finish()
                            activity.overridePendingTransition(0,0)
                        }
                }

                if(isActivityCanPreview(activity)){
                    activityPreviews.add(activity)
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
                if(isActivityCanPreview(activity)){
                    activityPreviews.remove(activity)
                }
            }
        })
    }

    val activityPreviews by lazy { LinkedList<Activity>() }
    private val activityWatcher by lazy { ArrayMap<Activity,SlideWatcher>() }

    private fun isActivityCanSlide(activity: Activity) =
        activity::class.java.isAnnotationPresent(SlideBackBinder::class.java)

    private fun isActivityCanPreview(activity: Activity) =
        activity::class.java.isAnnotationPresent(SlideBackPreview::class.java)

}