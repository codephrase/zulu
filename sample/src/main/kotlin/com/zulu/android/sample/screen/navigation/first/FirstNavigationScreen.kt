package com.zulu.android.sample.screen.navigation.first

import android.view.View
import android.widget.Button
import androidx.annotation.LayoutRes
import com.zulu.android.Screen
import com.zulu.android.ScreenContext
import com.zulu.android.State
import com.zulu.android.annotation.NavigationScreen
import com.zulu.android.sample.R
import com.zulu.android.sample.constant.NavigationConstant
import com.zulu.android.sample.datamodel.Person

@NavigationScreen(NavigationConstant.FIRST_NAVIGATION)
class FirstNavigationScreen(
    context: ScreenContext
) : Screen(context) {
    override val layoutId: Int
        @LayoutRes get() = R.layout.screen_navigation_first

    private lateinit var state: FirstNavigationState

    override fun onCreateState(previousState: State?): State? {
        state = previousState as? FirstNavigationState
            ?: FirstNavigationState()
        return state
    }

    override fun onNavigated(params: Any?) {
        super.onNavigated(params)

        setTitle(context.getString(R.string.title_first_navigation))
    }

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)

        state.name = "John"
        state.age = 20

        val buttonFirst = view.findViewById<Button>(R.id.button_first)
        buttonFirst.setOnClickListener {
            val person = Person().apply { firstName = "John"; lastName = "Doe"; age = 25 }
            navigationManager.navigate(NavigationConstant.SECOND_NAVIGATION, person)
        }
    }
}