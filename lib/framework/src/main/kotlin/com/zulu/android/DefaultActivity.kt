package com.zulu.android

import android.os.Bundle
import android.view.Menu
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.appbar.AppBarLayout
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class DefaultActivity : AppCompatActivity(), CoroutineScope, NavigationHandler {
    private val job: Job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override val navigationManager: NavigationManager by lazy {
        NavigationManager(this, placeholderId, initialScreenName)
    }

    protected open val layoutId: Int
        @LayoutRes get() = R.layout.activity_default

    protected open val placeholderId: Int
        @IdRes get() = R.id.placeholder

    protected open val appBarId: Int
        @IdRes get() = R.id.app_bar

    protected open val menuId: Int
        @MenuRes get() = 0

    protected abstract val initialScreenName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (layoutId != 0)
            setContentView(layoutId)

        navigationManager.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (menuId != 0)
            menuInflater.inflate(menuId, menu)

        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        navigationManager.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()

        coroutineContext.cancelChildren()
    }

    override fun onBackPressed() {
        if (!navigationManager.onBackPressed())
            super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    internal fun initToolbar(
        @LayoutRes toolbarLayoutId: Int,
        @IdRes toolbarId: Int,
        @IdRes headerPlaceholderId: Int,
        @LayoutRes headerLayoutId: Int
    ) {
        if (appBarId != 0) {
            val appBarLayout = findViewById<AppBarLayout>(appBarId)
            appBarLayout.removeAllViews()

            if (toolbarLayoutId != 0) {
                val view = layoutInflater.inflate(toolbarLayoutId, appBarLayout, false)
                appBarLayout.addView(view)

                if (toolbarId != 0) {
                    val toolbar = view.findViewById<Toolbar>(toolbarId)
                    setSupportActionBar(toolbar)
                }

                if (headerPlaceholderId != 0 && headerLayoutId != 0) {
                    val headerPlaceholder = view.findViewById<ViewGroup>(headerPlaceholderId)
                    headerPlaceholder?.let {
                        val headerView = layoutInflater.inflate(headerLayoutId, it, false)
                        it.addView(headerView)
                    }
                }
            }
        }
    }
}