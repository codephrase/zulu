package com.zulu.android

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

abstract class DefaultActivity : AppCompatActivity(), NavigationHandler {
    override val navigationManager: NavigationManager by lazy {
        NavigationManager(this, containerId, initialScreenName)
    }

    protected open val layoutId: Int
        @LayoutRes get() = 0

    protected open val containerId: Int
        @IdRes get() = 0

    protected open val toolbarId: Int
        @IdRes get() = 0

    protected abstract val initialScreenName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (layoutId != 0)
            setContentView(layoutId)

        if (toolbarId != 0) {
            val toolbar = findViewById<Toolbar>(toolbarId)
            setSupportActionBar(toolbar)
        }

        navigationManager.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        navigationManager.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        if (!navigationManager.onBackPressed())
            super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}