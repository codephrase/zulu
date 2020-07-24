package com.zulu.android.sample.screen.navigation.second

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.zulu.android.Screen
import com.zulu.android.ScreenContext
import com.zulu.android.State
import com.zulu.android.annotation.NavigationScreen
import com.zulu.android.sample.R
import com.zulu.android.sample.constant.NavigationConstant
import com.zulu.android.sample.datamodel.Person

@NavigationScreen(NavigationConstant.SECOND_NAVIGATION)
class SecondNavigationScreen(
    context: ScreenContext
) : Screen(context) {
    override val layoutId: Int
        @LayoutRes get() = R.layout.screen_navigation_second

    private lateinit var state: SecondNavigationState

    private lateinit var textViewSecond: TextView
    private lateinit var buttonNextSecond: Button
    private lateinit var buttonPreviousSecond: Button

    override fun onCreateState(previousState: State?): State? {
        state = previousState as? SecondNavigationState
            ?: SecondNavigationState()
        return state
    }

    override fun onNavigated(params: Any?) {
        super.onNavigated(params)

        state.person = params as Person

        setTitle(context.getString(R.string.title_second_navigation))
    }

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)

        textViewSecond = view.findViewById(R.id.text_view_second)
        textViewSecond.text = "${state.person?.firstName} ${state.person?.lastName}"

        buttonNextSecond = view.findViewById(R.id.button_next_second)
        buttonNextSecond.setOnClickListener {
            navigationManager.navigate(NavigationConstant.THIRD_NAVIGATION)
        }

        buttonPreviousSecond = view.findViewById(R.id.button_previous_second)
        buttonPreviousSecond.setOnClickListener {
            navigationManager.goBack()
        }
    }

    override fun onStateChanged(propertyName: String) {
        super.onStateChanged(propertyName)

        if (propertyName == "person") {
            textViewSecond.text = "${state.person?.firstName} ${state.person?.lastName}"
        }
    }

    override fun onResultReceived(result: Any?) {
        super.onResultReceived(result)

        val person = state.person ?: Person()
        person.firstName = "Jane"

        state.person = person
    }
}