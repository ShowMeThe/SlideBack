package com.show.example

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Fade
import android.transition.Slide
import android.transition.TransitionSet
import android.view.Gravity
import android.view.Window
import android.view.animation.LinearInterpolator
import com.show.slideback.annotation.SlideBackBinder
import com.show.slideback.annotation.SlideBackPreview
import kotlinx.android.synthetic.main.activity_main2.*

@SlideBackPreview
@SlideBackBinder
class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        val transition = Slide().apply {
            slideEdge = Gravity.END
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
        setContentView(R.layout.activity_main2)

        btn.setOnClickListener {
            startActivity(Intent(this, MainActivity3::class.java))
        }

    }
}