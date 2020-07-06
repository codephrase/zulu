package com.zulu.android

import android.os.Bundle
import com.squareup.moshi.Moshi

class DefaultObjectConverter : ObjectConverter {
    private val moshi = Moshi.Builder()
        .also {
            JsonAdapterManager.applyTo(it)
        }.build()

    override fun fromBundle(bundle: Bundle?): Any? {
        bundle?.let {
            val type = Class.forName(it.getString(TYPE) ?: "")
            val str = it.getString(DATA, "")

            val jsonAdapter = moshi.adapter(type)
            return jsonAdapter.fromJson(str)
        }

        return null
    }

    override fun toBundle(obj: Any?): Bundle? {
        obj?.let {
            val jsonAdapter = moshi.adapter(it.javaClass)
            val str = jsonAdapter.toJson(it)

            return Bundle().apply {
                putString(TYPE, it.javaClass.name)
                putString(DATA, str)
            }
        }

        return null
    }

    companion object {
        private const val TYPE = "type"
        private const val DATA = "data"
    }
}