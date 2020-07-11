package com.zulu.android.sample.screen.first

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.zulu.android.Screen
import com.zulu.android.ScreenContext
import com.zulu.android.State
import com.zulu.android.annotation.NavigationScreen
import com.zulu.android.sample.R
import com.zulu.android.sample.constant.NavigationConstant
import com.zulu.android.sample.datamodel.Person

@NavigationScreen(NavigationConstant.FIRST)
class FirstScreen(
    context: ScreenContext
) : Screen(context) {
    private lateinit var state: FirstState

    override fun onCreateState(previousState: State?): State? {
        state = previousState as? FirstState ?: FirstState()
        return state
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): View? {
        return inflater.inflate(R.layout.screen_first, container, false)
    }

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)

        state.name = "John"
        state.age = 20

        val buttonFirst = view.findViewById<Button>(R.id.button_first)
        buttonFirst.setOnClickListener {
            val person = Person().apply { firstName = "John"; lastName = "Doe"; age = 25 }
            navigationManager.navigate(NavigationConstant.SECOND, person)
        }
    }
}