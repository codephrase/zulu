package com.zulu.android

import androidx.lifecycle.MutableLiveData

class PersistenceLiveData<T>(
    private val store: Store,
    private val key: String,
    value: T? = null
) : MutableLiveData<T>(value) {
    override fun setValue(value: T) {
        store.set(key, value)
        super.setValue(value)
    }
}