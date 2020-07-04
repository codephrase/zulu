package com.zulu.android

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

object JsonAdapterManager {
    private val factories = mutableListOf<JsonAdapter.Factory>()
    private val adapters = mutableListOf<Any>()

    fun register(factory: JsonAdapter.Factory) {
        factories.add(factory)
    }

    fun register(adapter: Any) {
        adapters.add(adapter)
    }

    internal fun applyTo(builder: Moshi.Builder) {
        factories.forEach {
            builder.add(it)
        }

        adapters.forEach {
            builder.add(it)
        }
    }
}