package com.show.example

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.show.slideback.annotation.SlideBackPreview

@SlideBackPreview
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn).setOnClickListener {
            startActivity(Intent(this, MainActivity2::class.java))

            it.postDelayed({
                (it as Button).text = "onPause"
            }, 2500)
        }


    }
}