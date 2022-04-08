package com.example.KidsDrawhub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.AnimationUtils.loadAnimation
import android.widget.ImageView
import android.widget.TextView


class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        //this one is for hiding taskBar

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()

        //variables for animation
        // Animation topAnim,bottomAnim
        val backgroundImg : ImageView = findViewById(R.id.iv_logo)
        val topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation)
        backgroundImg.startAnimation(topAnim)

        val textView1 : TextView = findViewById(R.id.textView1)
        val bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_navigation)
        textView1.startAnimation(bottomAnim)



        Handler().postDelayed({
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        },3000)
    }
}