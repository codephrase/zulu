package com.zulu.android.sample

import android.view.Menu
import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.zulu.android.DefaultActivity
import com.zulu.android.sample.constant.NavigationConstant

class MainActivity : DefaultActivity() {
    override val layoutId: Int
        @LayoutRes get() = R.layout.activity_main

    override val containerId: Int
        @IdRes get() = R.id.container

    override val toolbarId: Int
        @IdRes get() = R.id.toolbar

    override val initialScreenName: String
        get() = NavigationConstant.FIRST

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
}
