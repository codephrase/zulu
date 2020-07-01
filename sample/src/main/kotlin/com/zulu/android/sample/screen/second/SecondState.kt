package com.zulu.android.sample.screen.second

import androidx.lifecycle.MutableLiveData
import com.zulu.android.BaseState
import com.zulu.android.Store
import com.zulu.android.sample.datamodel.Person

class SecondState constructor(
    store: Store
) : BaseState(store) {
    val person: MutableLiveData<Person>
        get() = store.getLiveData("person")
}