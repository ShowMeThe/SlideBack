package com.show.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.show.slideback.annotation.SlideBackBinder

@SlideBackBinder
class MainActivity3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
    }
}