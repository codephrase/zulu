package com.zulu.android

open class State {
    @Transient
    private val observers: MutableList<Observer> = mutableListOf()

    internal fun observe(observer: Observer) {
        observers.add(observer)
    }

    internal fun removeObserver(observer: Observer) {
        observers.remove(observer)
    }

    protected fun notifyPropertyChanged(propertyName: String) {
        observers.forEach {
            it.onChanged(propertyName)
        }
    }
}