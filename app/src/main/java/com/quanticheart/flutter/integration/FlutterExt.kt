package com.quanticheart.flutter.integration

import android.app.Activity
import android.app.Application
import android.util.Log
import io.flutter.FlutterInjector
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.FlutterEngineGroup
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

//
// Created by Jonn Alves on 27/12/22.
//
internal const val FLUTTER_ENGINE_ID = "flutter_engine"
lateinit var flutterEngine: FlutterEngine
lateinit var engines: FlutterEngineGroup

fun Application.initFlutterEngine() {
    flutterEngine = FlutterEngine(this)
    flutterEngine.dartExecutor.executeDartEntrypoint(
        DartExecutor.DartEntrypoint.createDefault()
    )
    FlutterEngineCache
        .getInstance()
        .put(FLUTTER_ENGINE_ID, flutterEngine)

    engines = FlutterEngineGroup(this)
}

class EngineBindings(activity: Activity, entrypoint: String) {
    val engine: FlutterEngine

    init {
        val app = activity.applicationContext as MyApplication
        val dartEntrypoint =
            DartExecutor.DartEntrypoint(
                FlutterInjector.instance().flutterLoader().findAppBundlePath(), entrypoint
            )
        engine = engines.createAndRunEngine(activity, dartEntrypoint)
    }
}

fun Activity.startActivityFlutterMain() {
    startActivity(
        FlutterActivity
            .withCachedEngine(FLUTTER_ENGINE_ID)
            .build(this)
    )
}

fun Activity.startActivityFlutter(routeString: String = "screenOne") {
    startActivity(
        FlutterActivity
            .withNewEngine()
            .initialRoute(routeString)
            .build(this)
    )
}

fun testCallFlutterMethod2(value1: Int, value2: Int) {
    val args = mapOf<String, Any>(
        "value1" to value1,
        "value2" to value2,
    )
    callFlutter("flutterIntPlus", args) { result ->
        val resultCast = result as Map<String, Any>
        Log.d(
            "Flutter",
            "Kotlin flutterIntPlus result from Flutter => ${resultCast.getOrElse("result") { 0 }}"
        )
    }
}

fun callFlutter(name: String, arguments: Any? = null, callback: (Any?) -> Unit) {
    val methodChannel = MethodChannel(
        flutterEngine.dartExecutor.binaryMessenger,
        "$defaultNativeChannel/methods_for_native"
    )
    methodChannel.setMethodCallHandler { call: MethodCall, result: MethodChannel.Result ->
        Log.d("Flutter", "Kotlin => method ${call.method} handle from Flutter")
        if (call.method == "callbackMethodFromFlutter") {
            callback(call.arguments)
            result.success(true)
        } else {
            result.success(false)
        }
    }

    methodChannel.invokeMethod(
        name,
        arguments,
        object : MethodChannel.Result {
            override fun success(o: Any?) {
                Log.d("Flutter", "Return Callback success => $o")
            }

            override fun error(errorCode: String, errorMessage: String?, errorDetails: Any?) {
                Log.d("Flutter", "Return Callback error => $errorMessage" ?: " ERROR")
            }

            override fun notImplemented() {
                Log.d("Flutter", "Return Callback notImplemented")
            }
        })
}
