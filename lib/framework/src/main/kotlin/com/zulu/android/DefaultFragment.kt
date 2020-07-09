package com.zulu.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

open class DefaultFragment : Fragment(), NavigationHandler {
    private lateinit var screenId: String
    private lateinit var screen: Screen
    private var state: State? = null

    override val navigationManager: NavigationManager
        get() {
            if (activity is NavigationHandler) {
                val navigationHandler = activity as NavigationHandler
                return navigationHandler.navigationManager
            }

            throw NullPointerException()
        }

    private val stateHandler: StateHandler
        get() = NavigationManager.resolveScreenStateHandler()

    private val observer = object : Observer {
        override fun onChanged(propertyName: String) {
            screen.onStateChanged(propertyName)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            val data = NavigationData(it)

            screenId = data.id

            val shouldRestore = stateHandler.contains(screenId)
            val previousState =
                if (shouldRestore)
                    stateHandler.restore(screenId) as? State
                else
                    null

            val screenContext = ScreenContext(requireContext(), this, data.id, data.name)

            screen = NavigationManager.resolveScreen(screenContext)
            state = screen.onCreateState(previousState)

            if (!shouldRestore)
                screen.onNavigated(data.params)

            return screen.onCreateView(inflater, container)
        }

        return null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        state?.observe(observer)
        screen.onViewCreated(view)

        if (navigationManager.shouldHandleScreenResult())
            screen.onResultReceived(navigationManager.getScreenResult())
    }

    override fun onStop() {
        super.onStop()

        stateHandler.save(screenId, state)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        state?.removeObserver(observer)
        screen.onDestroyView()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )

        screen.onRequestPermissionsResult(
            requestCode,
            permissions.toList(),
            grantResults.toList()
        )
    }

    fun onBackPressed(): Boolean {
        return screen.onBackPressed()
    }
}
