package com.show.example

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Fade
import android.transition.Slide
import android.transition.TransitionSet
import android.view.Gravity
import android.view.Window
import android.view.animation.LinearInterpolator
import android.widget.Button
import com.show.slideback.annotation.SlideBackPreview

@SlideBackPreview
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        val transition = Slide().apply {
            slideEdge = Gravity.START
        }
        val set = TransitionSet()
            .setDuration(280L)
            .apply {
                if (transition != null) {
                    addTransition(transition)
                }
            }
            .addTransition(Fade())
            .setInterpolator(LinearInterpolator())
            .setOrdering(TransitionSet.ORDERING_TOGETHER)
        window.exitTransition = set
        window.enterTransition = set
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn).setOnClickListener {
            startActivity(
                Intent(this, MainActivity2::class.java),
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
            )
        }


    }
}