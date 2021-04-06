package com.show.example

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.show.slideback.annotation.SlideBackBinder
import com.show.slideback.annotation.SlideBackPreview
import kotlinx.android.synthetic.main.activity_main2.*

@SlideBackPreview
@SlideBackBinder
class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        btn.setOnClickListener {
            startActivity(Intent(this, MainActivity3::class.java))
        }

    }
}