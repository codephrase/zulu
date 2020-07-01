package com.zulu.android

import androidx.lifecycle.MutableLiveData

class Store constructor(
    private val map: HashMap<String, Any> = hashMapOf()
) {
    private val liveDataCache: HashMap<String, PersistenceLiveData<*>> = hashMapOf()

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(key: String): T? {
        return map[key] as? T
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getLiveData(key: String): MutableLiveData<T> {
        if (liveDataCache.containsKey(key))
            return liveDataCache[key] as PersistenceLiveData<T>

        val liveData = PersistenceLiveData<T>(this, key, get(key))
        liveDataCache[key] = liveData
        return liveData
    }

    fun set(key: String, value: Any?) {
        value?.let {
            map[key] = it
        } ?: run {
            map.remove(key)
        }
    }

    fun toMap(): Map<String, Any> {
        return map
    }
}