package com.show.example

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.show.slideback.annotation.SlideBackBinder
import com.show.slideback.annotation.SlideBackPreview
import kotlinx.android.synthetic.main.activity_main2.*

@SlideBackPreview
@SlideBackBinder
class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)


        ivLogo.setOnClickListener { 
            Toast.makeText(this,"test",Toast.LENGTH_LONG).show()
        }

    }
}