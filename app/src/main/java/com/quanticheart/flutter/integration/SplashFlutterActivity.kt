package com.quanticheart.flutter.integration

import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

//
// Created by Jonn Alves on 28/12/22.
//
class SplashFlutterActivity : AppCompatActivity() {
    private val SPLASH_SCREEN_TIME_OUT = 4000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        startActivityFlutter("splashRoute")

        Handler().postDelayed({
            finish()
        }, SPLASH_SCREEN_TIME_OUT.toLong())
    }
}
