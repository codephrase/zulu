package com.zulu.android

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner

abstract class BaseScreen(
    screenContext: ScreenContext
) : NavigationHandler {
    val context: Context = screenContext.context
    val lifecycleOwner: LifecycleOwner = screenContext.lifecycleOwner
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

    fun requestPermissions(
        permissions: List<String>,
        requestCode: Int
    ) {
        val fragment = lifecycleOwner as? Fragment
        fragment?.requestPermissions(permissions.toTypedArray(), requestCode)
    }

    open fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: List<String>,
        grantResults: List<Int>
    ) {

    }
}