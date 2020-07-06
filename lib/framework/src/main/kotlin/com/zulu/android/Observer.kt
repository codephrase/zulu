package com.zulu.android

interface Observer {
    fun onChanged(propertyName: String)
}