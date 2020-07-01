package com.zulu.android

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseScreen(
    screenContext: ScreenContext
) : NavigationHandler {
    val context: Context = screenContext.context
    val id: String = screenContext.id
    val store: Store = screenContext.store

    override val navigationManager: NavigationManager
        get() {
            if (context is NavigationHandler) {
                val navigationHandler = context as NavigationHandler
                return navigationHandler.navigationManager
            }

            throw NullPointerException()
        }

    open fun onCreateState(): Any? {
        return null
    }

    open fun onNavigated(params: Any?) {

    }

    open fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): View? {
        return null
    }

    open fun onViewCreated(view: View) {

    }

    open fun onDestroyView() {

    }

    open fun onResultReceived(result: Any?) {

    }
}