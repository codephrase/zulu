package com.zulu.android.sample.screen.third

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.zulu.android.Screen
import com.zulu.android.ScreenContext
import com.zulu.android.annotation.NavigationScreen
import com.zulu.android.sample.R
import com.zulu.android.sample.constant.NavigationConstant

@NavigationScreen(NavigationConstant.THIRD)
class ThirdScreen(
    context: ScreenContext
) : Screen(context) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): View? {
        return inflater.inflate(R.layout.screen_third, container, false)
    }

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)

        val buttonThird = view.findViewById<Button>(R.id.button_third)
        buttonThird.setOnClickListener {
            navigationManager.goBack()
        }
    }
}