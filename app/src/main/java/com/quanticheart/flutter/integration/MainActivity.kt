package com.quanticheart.flutter.integration

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn = findViewById<Button>(R.id.btn)
        val btnSplash = findViewById<Button>(R.id.btnSplash)
        val btnCallback = findViewById<Button>(R.id.btnCallback)
        val btnCallbackMethod = findViewById<Button>(R.id.btnCallbackMethod)
        btn.setOnClickListener {
            startActivityFlutter()
        }

        btnSplash.setOnClickListener {
            startActivity(Intent(this, SplashFlutterActivity::class.java))
        }

        btnCallback.setOnClickListener {
            startActivity(
                Intent(this, SearchActivity::class.java)
            )
        }

        btnCallbackMethod.setOnClickListener {
            testCallFlutterMethod2(9, 6)
        }
    }
}