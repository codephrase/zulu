package com.zulu.android.sample.screen.navigation.third

import android.view.View
import android.widget.Button
import androidx.annotation.LayoutRes
import com.zulu.android.Screen
import com.zulu.android.ScreenContext
import com.zulu.android.annotation.NavigationScreen
import com.zulu.android.sample.R
import com.zulu.android.sample.constant.NavigationConstant

@NavigationScreen(NavigationConstant.THIRD_NAVIGATION)
class ThirdNavigationScreen(
    context: ScreenContext
) : Screen(context) {
    override val layoutId: Int
        @LayoutRes get() = R.layout.screen_navigation_third

    override fun onNavigated(params: Any?) {
        super.onNavigated(params)

        setTitle(context.getString(R.string.title_third_navigation))
        setSubtitle(context.getString(R.string.subtitle_third_navigation))
    }

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)

        val buttonThird = view.findViewById<Button>(R.id.button_third)
        buttonThird.setOnClickListener {
            navigationManager.goBack()
        }
    }
}