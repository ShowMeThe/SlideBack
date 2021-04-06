package com.show.slideback

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.ArrayMap
import android.util.Log
import com.show.slideback.annotation.SlideBackBinder
import com.show.slideback.annotation.SlideBackPreview
import com.show.slideback.util.Config
import com.show.slideback.util.Utils
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
        lateinit var application:Application
        val register: SlideRegister by lazy { SlideRegister() }
        fun init(application: Application) {
            this.application = application
            register.registerApplication()
        }

        fun config(onConfig: Config.()->Unit){
            onConfig.invoke(Config.getConfig())
        }
    }

    private fun registerApplication() {
        application.registerActivityLifecycleCallbacks(object :
            Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                if(isActivityCanPreview(activity)){
                    activityPreviews.add(SliderPreWatch(activity))
                }
                if(isActivityCanSlide(activity)){
                    activityWatcher[activity] = SlideWatcher(activity)
                        .setOnSliderBackListener{
                            activity.finish()
                            activity.overridePendingTransition(0,0)
                        }
                }
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityResumed(activity: Activity) {
            }

            override fun onActivityPaused(activity: Activity) {
                if(isActivityCanPreview(activity)){
                    val index = activityPreviews.indexOf(SliderPreWatch(activity))
                    if(index >= 0){
                        activityPreviews[index].contentView = Utils.getContentView(activity)
                    }
                }
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityDestroyed(activity: Activity) {
                if(isActivityCanPreview(activity)){
                    activityPreviews.remove(SliderPreWatch(activity))
                }
                if(isActivityCanSlide(activity)){
                    activityWatcher.remove(activity)
                }
            }
        })
    }

    val activityPreviews by lazy { LinkedList<SliderPreWatch>() }
    private val activityWatcher by lazy { ArrayMap<Activity,SlideWatcher>() }

    private fun isActivityCanSlide(activity: Activity) =
        activity::class.java.isAnnotationPresent(SlideBackBinder::class.java)

    private fun isActivityCanPreview(activity: Activity) =
        activity::class.java.isAnnotationPresent(SlideBackPreview::class.java)

}