package com.zulu.android

import android.content.Context

interface StateHandler {
    fun initialize(context: Context, restore: Boolean)
    fun save(id: String, data: Any?)
    fun restore(id: String): Any?
    fun contains(id: String): Boolean
}