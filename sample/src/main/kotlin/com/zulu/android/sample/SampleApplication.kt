package com.zulu.android.sample

import android.app.Application

class SampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        ScreenInitializer.run()
    }
}