package com.zulu.android.sample.screen.navigation.first

import com.squareup.moshi.JsonClass
import com.zulu.android.State

@JsonClass(generateAdapter = true)
class FirstNavigationState : State() {
    var name: String = ""
        set(value) {
            field = value
            notifyPropertyChanged("name")
        }

    var age: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged("age")
        }
}