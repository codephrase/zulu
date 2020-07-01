package com.zulu.android

import android.content.Context
import android.content.SharedPreferences
import com.squareup.moshi.Moshi

class DefaultStateHandler : StateHandler {
    private lateinit var sharedPreferences: SharedPreferences

    private val moshi = Moshi.Builder()
        .add(StoreJsonAdapter.Factory())
        .build()

    override fun initialize(context: Context, restore: Boolean) {
        sharedPreferences = context.getSharedPreferences(SCREEN_STATE, Context.MODE_PRIVATE)

        if (!restore) {
            sharedPreferences
                .edit()
                .clear()
                .apply()
        }
    }

    override fun save(id: String, data: Any?) {
        var typeName: String? = null
        var str: String? = null

        data?.let {
            typeName = it.javaClass.name

            val jsonAdapter = moshi.adapter(it.javaClass)
            str = jsonAdapter.toJson(it)
        }

        sharedPreferences
            .edit()
            .putBoolean("$id", true)
            .putString("$id-${TYPE}", typeName)
            .putString("$id-${DATA}", str)
            .apply()
    }

    override fun restore(id: String): Any? {
        if (sharedPreferences.getBoolean("$id", false)) {
            val typeName = sharedPreferences.getString("$id-${TYPE}", null) ?: ""
            val str = sharedPreferences.getString("$id-${DATA}", null)

            sharedPreferences
                .edit()
                .remove("$id")
                .remove("$id-${TYPE}")
                .remove("$id-${DATA}")
                .apply()

            str?.let {
                val type = Class.forName(typeName)

                val jsonAdapter = moshi.adapter(type)
                return jsonAdapter.fromJson(str)
            }
        }

        return null
    }

    override fun contains(id: String): Boolean {
        return sharedPreferences.getBoolean("$id", false)
    }

    companion object {
        private const val TYPE = "type"
        private const val DATA = "data"

        private const val SCREEN_STATE = "screen-state"
    }
}