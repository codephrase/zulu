package com.zulu.android

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class Screen(
    screenContext: ScreenContext
) : CoroutineScope, NavigationHandler {
    val context: Context = screenContext.context
    val lifecycleOwner: LifecycleOwner = screenContext.lifecycleOwner
    val id: String = screenContext.id

    private val job: Job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override val navigationManager: NavigationManager
        get() {
            if (context is NavigationHandler) {
                val navigationHandler = context as NavigationHandler
                return navigationHandler.navigationManager
            }

            throw NullPointerException()
        }

    open val toolbarMode: ToolbarMode
        get() = ToolbarMode.DEFAULT

    open val toolbarLayoutId: Int
        @LayoutRes get() = 0

    open val toolbarId: Int
        @IdRes get() = R.id.toolbar

    open val headerPlaceholderId: Int
        @IdRes get() = R.id.header_placeholder

    open val headerLayoutId: Int
        @LayoutRes get() = 0

    open val menuId: Int
        @MenuRes get() = 0

    open val layoutId: Int
        @LayoutRes get() = 0

    open fun onCreateState(previousState: State?): State? {
        return previousState
    }

    open fun onNavigated(params: Any?) {

    }

    open fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): View? {
        if (layoutId != 0)
            return inflater.inflate(layoutId, container, false)

        return null
    }

    open fun onViewCreated(view: View) {

    }

    open fun onOptionsItemSelected(item: MenuItem): Boolean {
        return false
    }

    open fun onStateChanged(propertyName: String) {

    }

    open fun onDestroyView() {
        coroutineContext.cancelChildren()
    }

    open fun onResultReceived(result: Any?) {

    }

    open fun onBackPressed(): Boolean {
        return false
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

    fun setUpButtonEnabled(upButtonEnabled: Boolean) {
        val fragment = lifecycleOwner as? DefaultFragment
        fragment?.setUpButtonEnabled(upButtonEnabled)
    }

    fun setTitle(title: String) {
        val fragment = lifecycleOwner as? DefaultFragment
        fragment?.setTitle(title)
    }

    fun setSubtitle(subtitle: String) {
        val fragment = lifecycleOwner as? DefaultFragment
        fragment?.setSubtitle(subtitle)
    }
}