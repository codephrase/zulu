package com.zulu.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

open class DefaultFragment : Fragment(), NavigationHandler {
    private lateinit var screenId: String
    private lateinit var screen: BaseScreen

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            val data = NavigationData(it)

            screenId = data.id

            val shouldRestore = stateHandler.contains(screenId)
            val store =
                if (shouldRestore)
                    stateHandler.restore(screenId) as Store
                else
                    Store()

            val screenContext = ScreenContext(requireContext(), this, data.id, data.name, store)

            screen = NavigationManager.resolveScreen(screenContext)
            screen.onCreateState()

            if (!shouldRestore)
                screen.onNavigated(data.params)

            return screen.onCreateView(inflater, container)
        }

        return null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        screen.onViewCreated(view)

        if (navigationManager.shouldHandleScreenResult())
            screen.onResultReceived(navigationManager.getScreenResult())
    }

    override fun onStop() {
        super.onStop()

        stateHandler.save(screenId, screen.store)
    }

    override fun onDestroyView() {
        super.onDestroyView()

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
}
