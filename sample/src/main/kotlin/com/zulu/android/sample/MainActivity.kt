package com.zulu.android.sample

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.zulu.android.NavigationHandler
import com.zulu.android.NavigationManager
import com.zulu.android.sample.constant.NavigationConstant
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationHandler {
    override val navigationManager = NavigationManager(this, containerId, initialScreenName)

    private val layoutId: Int
        @LayoutRes get() = R.layout.activity_main

    private val containerId: Int
        @IdRes get() = R.id.container

    private val initialScreenName: String
        get() = NavigationConstant.FIRST

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        setSupportActionBar(toolbar)

        navigationManager.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        navigationManager.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        if (!navigationManager.onBackPressed())
            super.onBackPressed()
    }
}
