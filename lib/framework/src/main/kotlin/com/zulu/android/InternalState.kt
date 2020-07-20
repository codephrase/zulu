package com.zulu.android

class InternalState : State() {
    var upButtonEnabled: Boolean? = null
        set(value) {
            field = value
            notifyPropertyChanged("upButtonEnabled")
        }

    var title: String? = null
        set(value) {
            field = value
            notifyPropertyChanged("title")
        }

    var subtitle: String? = null
        set(value) {
            field = value
            notifyPropertyChanged("subtitle")
        }
}