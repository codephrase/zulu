package com.zulu.android.sample.screen.toolbar.none

import androidx.annotation.LayoutRes
import com.zulu.android.Screen
import com.zulu.android.ScreenContext
import com.zulu.android.ToolbarMode
import com.zulu.android.annotation.NavigationScreen
import com.zulu.android.sample.R
import com.zulu.android.sample.constant.NavigationConstant

@NavigationScreen(NavigationConstant.NO_TOOLBAR)
class NoToolbarScreen(
    context: ScreenContext
) : Screen(context) {
    override val toolbarMode: ToolbarMode
        get() = ToolbarMode.NONE

    override val layoutId: Int
        @LayoutRes get() = R.layout.screen_toolbar
}