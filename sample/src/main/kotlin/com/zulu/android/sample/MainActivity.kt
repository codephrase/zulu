package com.zulu.android.sample

import android.view.MenuItem
import androidx.annotation.MenuRes
import com.zulu.android.DefaultActivity
import com.zulu.android.sample.constant.NavigationConstant

class MainActivity : DefaultActivity() {
    override val menuId: Int
        @MenuRes get() = R.menu.menu_main

    override val initialScreenName: String
        get() = NavigationConstant.HOME

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
