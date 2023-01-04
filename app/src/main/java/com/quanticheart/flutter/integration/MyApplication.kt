package com.quanticheart.flutter.integration

import android.app.Application

//
// Created by Jonn Alves on 27/12/22.
//

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initFlutterEngine()
    }
}

