package com.zulu.android.sample.screen.home

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zulu.android.Screen
import com.zulu.android.ScreenContext
import com.zulu.android.State
import com.zulu.android.annotation.NavigationScreen
import com.zulu.android.sample.R
import com.zulu.android.sample.constant.NavigationConstant

@NavigationScreen(NavigationConstant.HOME)
class HomeScreen(
    context: ScreenContext
) : Screen(context) {
    override val layoutId: Int
        @LayoutRes get() = R.layout.screen_home

    private lateinit var state: HomeState

    override fun onNavigated(params: Any?) {
        super.onNavigated(params)

        state.items = mutableListOf(
            Item(ItemType.SECTION, context.getString(R.string.section_navigation)),
            Item(
                ItemType.DEFAULT,
                context.getString(R.string.item_simple_navigation),
                NavigationConstant.FIRST_NAVIGATION
            ),
            Item(ItemType.SECTION, context.getString(R.string.section_toolbar)),
            Item(
                ItemType.DEFAULT,
                context.getString(R.string.item_default_toolbar),
                NavigationConstant.DEFAULT_TOOLBAR
            ),
            Item(
                ItemType.DEFAULT,
                context.getString(R.string.item_collapsing_toolbar),
                NavigationConstant.COLLAPSING_TOOLBAR
            ),
            Item(
                ItemType.DEFAULT,
                context.getString(R.string.item_no_toolbar),
                NavigationConstant.NO_TOOLBAR
            )
        )
    }

    override fun onCreateState(previousState: State?): State? {
        state = previousState as? HomeState
            ?: HomeState()
        return state
    }

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = HomeAdapter(state.items) {
            if (it.type == ItemType.DEFAULT) {
                it.tag?.let { screenName ->
                    navigationManager.navigate(screenName)
                }
            }
        }
    }
}