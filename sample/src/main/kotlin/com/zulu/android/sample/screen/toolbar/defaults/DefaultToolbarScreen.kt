package com.zulu.android.sample.screen.toolbar.defaults

import androidx.annotation.LayoutRes
import com.zulu.android.Screen
import com.zulu.android.ScreenContext
import com.zulu.android.annotation.NavigationScreen
import com.zulu.android.sample.R
import com.zulu.android.sample.constant.NavigationConstant

@NavigationScreen(NavigationConstant.DEFAULT_TOOLBAR)
class DefaultToolbarScreen(
    context: ScreenContext
) : Screen(context) {
    override val layoutId: Int
        @LayoutRes get() = R.layout.screen_toolbar

    override fun onNavigated(params: Any?) {
        super.onNavigated(params)

        setTitle(context.getString(R.string.title_default_toolbar))
    }
}