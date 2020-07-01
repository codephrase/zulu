package com.zulu.android

interface ScreenFactory {
    val name: String

    fun create(context: ScreenContext): BaseScreen
}