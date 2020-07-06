package com.zulu.android.helper

import android.os.Handler
import android.os.Looper
import java.util.*

object ThreadHelper {
    fun runOnUIThread(delayMillis: Long = 0, action: () -> Unit) {
        val handler = Handler(Looper.getMainLooper())

        if (delayMillis > 0)
            handler.postDelayed(action, delayMillis)
        else
            handler.post(action)
    }

    fun runOnBackgroundThread(delayMillis: Long = 0, action: () -> Unit) {
        val runThread: () -> Unit = {
            val thread = Thread(action)
            thread.start()
        }

        if (delayMillis > 0) {
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    runThread()
                }
            }, delayMillis)
        } else {
            runThread()
        }
    }
}