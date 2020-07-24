package com.zulu.android.sample.screen.toolbar.collapsing

import androidx.annotation.LayoutRes
import com.zulu.android.Screen
import com.zulu.android.ScreenContext
import com.zulu.android.ToolbarMode
import com.zulu.android.annotation.NavigationScreen
import com.zulu.android.sample.R
import com.zulu.android.sample.constant.NavigationConstant

@NavigationScreen(NavigationConstant.COLLAPSING_TOOLBAR)
class CollapsingToolbarScreen(
    context: ScreenContext
) : Screen(context) {
    override val toolbarMode: ToolbarMode
        get() = ToolbarMode.COLLAPSING

    override val headerLayoutId: Int
        @LayoutRes get() = R.layout.header_toolbar

    override val layoutId: Int
        @LayoutRes get() = R.layout.screen_toolbar

    override fun onNavigated(params: Any?) {
        super.onNavigated(params)

        setTitle(context.getString(R.string.title_collapsing_toolbar))
    }
}