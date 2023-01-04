package com.quanticheart.flutter.integration

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts

class SearchActivity : ComponentActivity() {
    private lateinit var response: TextView
    private lateinit var flipper: ViewFlipper

    private val launch =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                response.text = intent?.getStringExtra("response")
                flipper.displayedChild = 1
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val text = findViewById<EditText>(R.id.text)
        val btn = findViewById<Button>(R.id.btn)
        val refresh = findViewById<Button>(R.id.refresh)

        response = findViewById(R.id.response)
        flipper = findViewById(R.id.flipper)

        btn.setOnClickListener {
            launch.launch(
                Intent(this, FlutterCustonActivity::class.java).apply {
                    putExtra("search", text.text.toString())
                }
            )
        }

        refresh.setOnClickListener {
            flipper.displayedChild = 0
            text.setText("")
        }
    }
}