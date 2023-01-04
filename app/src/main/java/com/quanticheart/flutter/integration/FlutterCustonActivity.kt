package com.quanticheart.flutter.integration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import java.util.*

//
// Created by Jonn Alves on 28/12/22.
//
const val defaultNativeChannel = "com.quanticheart.flutter"

class FlutterCustonActivity : FlutterActivity() {
    private val channelSendSearch = "$defaultNativeChannel/search_details"
    private val channelCallbackSelected = "$defaultNativeChannel/search_details_callback"

    private lateinit var methodChannel: MethodChannel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        methodChannel =
            MethodChannel(flutterEngine!!.dartExecutor.binaryMessenger, channelSendSearch).apply {
                setMethodCallHandler { call: MethodCall, result: MethodChannel.Result ->
                    if (call.method == "getSearchFields") {
                        getSearchFields()
                        result.success(true)
                    } else {
                        result.success(false)
                    }
                }
            }

        MethodChannel(flutterEngine!!.dartExecutor.binaryMessenger, channelCallbackSelected).apply {
            setMethodCallHandler { call: MethodCall, result: MethodChannel.Result ->
                if (call.method == "callbackSelected") {
                    callbackSelected(call.arguments<Map<String, Any>>())
                    result.success(true)
                } else {
                    result.success(false)
                }
            }
        }

        val engineId = UUID.randomUUID().toString()
        val engine = provideFlutterEngine(this)
        FlutterEngineCache.getInstance().put(engineId, engine)
    }

    private fun getSearchFields() {
        val text = intent?.extras?.getString("search") ?: "Nothing"
        val data = mapOf<String, Any>(
            "text" to text
        )
        methodChannel.invokeMethod("getSearchFields", data)
    }

    private fun callbackSelected(arguments: Map<String, Any>?) {
        val status = arguments?.get("result") as String
        Toast.makeText(baseContext, status, Toast.LENGTH_SHORT).show()
        val data = Intent()
        data.putExtra("response", status)
        setResult(RESULT_OK, data)
        finish()
    }

    private val engineBindings: EngineBindings by lazy {
        EngineBindings(activity = this, entrypoint = "screenCallback")
    }

    override fun provideFlutterEngine(context: Context): FlutterEngine {
        return engineBindings.engine
    }
}