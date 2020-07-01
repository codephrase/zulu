package com.zulu.android

import android.os.Bundle
import java.util.*

class NavigationData {
    var id: String = ""
        private set

    var name: String = ""
        private set

    var params: Any? = null
        private set

    private val converter: ObjectConverter
        get() = NavigationManager.resolveObjectConverter()

    constructor(name: String, params: Any? = null) {
        this.id = UUID.randomUUID().toString()
        this.name = name
        this.params = params
    }

    constructor(bundle: Bundle) {
        this.id = bundle.getString(ID, "")
        this.name = bundle.getString(NAME, "")
        this.params = convertFromBundle(bundle.getBundle(PARAMS))
    }

    fun toBundle(): Bundle {
        return Bundle().apply {
            putString(ID, id)
            putString(NAME, name)
            putBundle(PARAMS, convertToBundle(params))
        }
    }

    private fun convertToBundle(obj: Any?): Bundle? {
        return converter.toBundle(obj)
    }

    private fun convertFromBundle(bundle: Bundle?): Any? {
        return converter.fromBundle(bundle)
    }

    companion object {
        private const val ID = "id"
        private const val NAME = "name"
        private const val PARAMS = "params"
    }
}