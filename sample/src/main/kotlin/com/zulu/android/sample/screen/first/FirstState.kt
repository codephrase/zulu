package com.zulu.android.sample.screen.first

import com.zulu.android.BaseState
import com.zulu.android.Store

class FirstState constructor(
    store: Store
) : BaseState(store) {
    var name: String
        get() = store.get("name") ?: ""
        set(value) = store.set("name", value)

    var age: Int
        get() = store.get("age") ?: 0
        set(value) = store.set("age", value)
}