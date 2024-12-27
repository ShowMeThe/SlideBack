package com.show.example

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.show.slideback.annotation.SlideBackBinder
import com.show.slideback.annotation.SlideBackPreview

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


        findViewById<View>(R.id.ivLogo).setOnClickListener {
            Toast.makeText(this,"test",Toast.LENGTH_LONG).show()
        }

    }
}