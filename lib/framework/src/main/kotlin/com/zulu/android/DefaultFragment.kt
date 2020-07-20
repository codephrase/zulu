package com.zulu.android

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

open class DefaultFragment : Fragment(), NavigationHandler {
    private lateinit var internalStateId: String
    private lateinit var screenId: String
    private lateinit var screen: Screen
    private var internalState: InternalState? = null
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

    private val internalObserver = object : Observer {
        override fun onChanged(propertyName: String) {
            onInternalStateChanged(propertyName)
        }
    }

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

            internalStateId = "${data.id}-${INTERNAL}"
            screenId = data.id

            val shouldRestore = stateHandler.contains(internalStateId)

            val previousInternalState =
                if (shouldRestore)
                    stateHandler.restore(internalStateId) as? InternalState
                else
                    null

            val previousState =
                if (stateHandler.contains(screenId))
                    stateHandler.restore(screenId) as? State
                else
                    null

            val screenContext = ScreenContext(requireContext(), this, data.id, data.name)

            screen = NavigationManager.resolveScreen(screenContext)
            internalState = previousInternalState ?: InternalState()
            state = screen.onCreateState(previousState)

            if (!shouldRestore)
                screen.onNavigated(data.params)

            val view = screen.onCreateView(inflater, container)

            initToolbar()
            setHasOptionsMenu(screen.menuId != 0)

            return view
        }

        return null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        internalState?.observe(internalObserver)
        state?.observe(observer)
        screen.onViewCreated(view)

        if (navigationManager.shouldHandleScreenResult())
            screen.onResultReceived(navigationManager.getScreenResult())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        if (screen.menuId != 0)
            inflater.inflate(screen.menuId, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return screen.onOptionsItemSelected(item)
    }

    override fun onStop() {
        super.onStop()

        stateHandler.save(internalStateId, internalState)
        stateHandler.save(screenId, state)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        internalState?.removeObserver(internalObserver)
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

    private fun onInternalStateChanged(propertyName: String) {
        when (propertyName) {
            "upButtonEnabled" -> invalidateUpButtonEnabled()
            "title" -> invalidateTitle()
            "subtitle" -> invalidateSubtitle()
        }
    }

    private fun initToolbar() {
        invalidateUpButtonEnabled()
        invalidateTitle()
        invalidateSubtitle()
    }

    private fun invalidateUpButtonEnabled() {
        val supportActionBar = (context as AppCompatActivity).supportActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(
            internalState?.upButtonEnabled ?: navigationManager.canGoBack()
        )
    }

    private fun invalidateTitle() {
        val supportActionBar = (context as AppCompatActivity).supportActionBar
        supportActionBar?.let {
            internalState?.title?.let { title ->
                it.title = title
            } ?: run {
                val activity = context as AppCompatActivity
                val packageManager = activity.packageManager

                it.title = packageManager
                    .getActivityInfo(
                        activity.componentName,
                        PackageManager.GET_META_DATA
                    )
                    .loadLabel(packageManager)
                    .toString()
            }
        }
    }

    private fun invalidateSubtitle() {
        val supportActionBar = (context as AppCompatActivity).supportActionBar
        supportActionBar?.subtitle = internalState?.subtitle
    }

    fun setUpButtonEnabled(upButtonEnabled: Boolean) {
        internalState?.upButtonEnabled = upButtonEnabled
    }

    fun setTitle(title: String) {
        internalState?.title = title
    }

    fun setSubtitle(subtitle: String) {
        internalState?.subtitle = subtitle
    }

    companion object {
        private const val INTERNAL = "internal"
    }
}
