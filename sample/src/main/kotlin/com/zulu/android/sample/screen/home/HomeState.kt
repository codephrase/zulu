package com.zulu.android.sample.screen.home

import com.squareup.moshi.JsonClass
import com.zulu.android.State

@JsonClass(generateAdapter = true)
class HomeState : State() {
    var items: List<Item> = mutableListOf()
        set(value) {
            field = value
            notifyPropertyChanged("items")
        }
}