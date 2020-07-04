package com.zulu.android.sample.screen.second

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import com.zulu.android.BaseScreen
import com.zulu.android.ScreenContext
import com.zulu.android.annotation.Screen
import com.zulu.android.sample.R
import com.zulu.android.sample.constant.NavigationConstant
import com.zulu.android.sample.datamodel.Person

@Screen(NavigationConstant.SECOND)
class SecondScreen(
    context: ScreenContext
) : BaseScreen(context) {
    private lateinit var state: SecondState

    override fun onCreateState(): Any? {
        state = SecondState(store)
        return state
    }

    override fun onNavigated(params: Any?) {
        super.onNavigated(params)

        state.person.value = params as Person
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): View? {
        return inflater.inflate(R.layout.screen_second, container, false)
    }

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)

        val textViewSecond = view.findViewById<TextView>(R.id.text_view_second)
        state.person.observe(lifecycleOwner, Observer {
            val person = state.person.value
            textViewSecond.text = "${person?.firstName} ${person?.lastName}"
        })

        val buttonNextSecond = view.findViewById<Button>(R.id.button_next_second)
        buttonNextSecond.setOnClickListener {
            navigationManager.navigate(NavigationConstant.THIRD)
        }

        val buttonPreviousSecond = view.findViewById<Button>(R.id.button_previous_second)
        buttonPreviousSecond.setOnClickListener {
            navigationManager.goBack()
        }
    }

    override fun onResultReceived(result: Any?) {
        super.onResultReceived(result)

        val person = state.person.value ?: Person()
        person.firstName = "Jane"

        state.person.value = person
    }
}