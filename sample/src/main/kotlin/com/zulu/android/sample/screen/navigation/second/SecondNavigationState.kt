package com.zulu.android.sample.screen.navigation.second

import com.squareup.moshi.JsonClass
import com.zulu.android.State
import com.zulu.android.sample.datamodel.Person

@JsonClass(generateAdapter = true)
class SecondNavigationState : State() {
    var person: Person? = null
        set(value) {
            field = value
            notifyPropertyChanged("person")
        }
}