package com.zulu.android

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import kotlin.reflect.KClass

class NavigationManager constructor(
    private val activity: AppCompatActivity,
    @IdRes private val containerId: Int,
    private val initialScreenName: String
) {
    private val fragmentManager: FragmentManager
        get() = activity.supportFragmentManager

    private val stateHandler: StateHandler
        get() = resolveScreenStateHandler()

    private lateinit var navigationStack: ArrayList<NavigationState>

    fun onCreate(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            navigationStack = it.getParcelableArrayList(NAVIGATION_STACK)
                ?: throw Exception("Invalid navigation state")

            stateHandler.initialize(activity, true)
        } ?: run {
            navigationStack = arrayListOf()

            stateHandler.initialize(activity, false)

            val data = NavigationData(initialScreenName)

            val fragment = DefaultFragment().apply {
                arguments = data.toBundle()
            }

            navigationStack.clear()
            navigationStack.add(NavigationState(data.id, data.name))

            fragmentManager
                .beginTransaction()
                .replace(containerId, fragment, data.id)
                .commit()
        }
    }

    fun onSaveInstanceState(state: Bundle) {
        state.putParcelableArrayList(NAVIGATION_STACK, navigationStack)
    }

    fun onBackPressed(): Boolean {
        val currentNavigationState = navigationStack.last()
        val currentFragment = fragmentManager.findFragmentByTag(currentNavigationState.id)
            ?: throw Exception("Invalid navigation state")

        if (currentFragment is DefaultFragment && currentFragment.onBackPressed()) {
            return true
        } else {
            if (navigationStack.size > 1) {
                goBack()
                return true
            }
        }

        return false
    }

    fun navigate(screenName: String, mode: NavigationMode = NavigationMode.DEFAULT) {
        navigate(screenName, null, mode)
    }

    fun navigate(screenName: String, params: Any?, mode: NavigationMode = NavigationMode.DEFAULT) {
        when (mode) {
            NavigationMode.REPLACE -> navigateReplace(screenName, params)
            NavigationMode.CLEAR_TOP -> navigateClearTop(screenName, params)
            NavigationMode.CLEAR_STACK -> navigateClearStack(screenName, params)
            else -> navigateDefault(screenName, params)
        }
    }

    private fun navigateDefault(screenName: String, params: Any?) {
        val data = NavigationData(screenName, params)

        val currentNavigationState = navigationStack.last()
        val currentFragment = fragmentManager.findFragmentByTag(currentNavigationState.id)
            ?: throw Exception("Invalid navigation state")

        val nextNavigationState = NavigationState(data.id, data.name)
        val nextFragment = DefaultFragment().apply {
            arguments = data.toBundle()
        }

        navigationStack.add(nextNavigationState)

        fragmentManager
            .beginTransaction()
            .detach(currentFragment)
            .replace(containerId, nextFragment, data.id)
            .commit()
    }

    private fun navigateReplace(screenName: String, params: Any?) {
        val data = NavigationData(screenName, params)

        val currentNavigationState = navigationStack.last()
        val currentFragment = fragmentManager.findFragmentByTag(currentNavigationState.id)
            ?: throw Exception("Invalid navigation state")

        navigationStack.removeAt(navigationStack.lastIndex)

        val nextNavigationState = NavigationState(data.id, data.name)
        val nextFragment = DefaultFragment().apply {
            arguments = data.toBundle()
        }

        navigationStack.add(nextNavigationState)

        fragmentManager
            .beginTransaction()
            .remove(currentFragment)
            .replace(containerId, nextFragment, data.id)
            .commit()
    }

    private fun navigateClearTop(screenName: String, params: Any?) {
        val index = navigationStack.indexOfLast { it.name == screenName }

        if (index >= 0) {
            val data = NavigationData(screenName, params)

            val fragmentTransaction = fragmentManager.beginTransaction()

            val backwardCount = navigationStack.size - index
            for (i in 1..backwardCount) {
                val currentNavigationState = navigationStack.removeAt(navigationStack.lastIndex)
                val currentFragment = fragmentManager.findFragmentByTag(currentNavigationState.id)
                    ?: throw Exception("Invalid navigation state")

                fragmentTransaction.remove(currentFragment)
            }

            val nextNavigationState = NavigationState(data.id, data.name)
            val nextFragment = DefaultFragment().apply {
                arguments = data.toBundle()
            }

            navigationStack.add(nextNavigationState)

            fragmentTransaction.replace(containerId, nextFragment, data.id)
            fragmentTransaction.commit()
        } else {
            navigateDefault(screenName, params)
        }
    }

    private fun navigateClearStack(screenName: String, params: Any?) {
        val data = NavigationData(screenName, params)

        val fragmentTransaction = fragmentManager.beginTransaction()

        for (i in navigationStack.lastIndex downTo 0) {
            val currentNavigationState = navigationStack[i]
            val currentFragment = fragmentManager.findFragmentByTag(currentNavigationState.id)
                ?: throw Exception("Invalid navigation state")

            fragmentTransaction.detach(currentFragment)
        }

        navigationStack.clear()

        val nextNavigationState = NavigationState(data.id, data.name)
        val nextFragment = DefaultFragment().apply {
            arguments = data.toBundle()
        }

        navigationStack.add(nextNavigationState)

        fragmentTransaction.replace(containerId, nextFragment, data.id)
        fragmentTransaction.commit()
    }

    fun goBack(params: Any? = null) {
        goBack(null, params)
    }

    fun goBack(screenName: String?, params: Any? = null) {
        val index =
            if (screenName != null)
                navigationStack.dropLast(1).indexOfLast { it.name == screenName }
            else
                navigationStack.lastIndex - 1

        if (index < 0) {
            if (screenName != null)
                throw Exception("No screen found in navigation stack: $screenName")
            else
                throw Exception("Navigation stack is empty")
        }

        setScreenResult(params)

        val fragmentTransaction = fragmentManager.beginTransaction()

        val backwardCount = navigationStack.lastIndex - index
        for (i in 1..backwardCount) {
            val currentNavigationState = navigationStack.removeAt(navigationStack.lastIndex)
            val currentFragment = fragmentManager.findFragmentByTag(currentNavigationState.id)
                ?: throw Exception("Invalid navigation state")

            fragmentTransaction.remove(currentFragment)
        }

        val nextNavigationState = navigationStack.last()
        val nextFragment = fragmentManager.findFragmentByTag(nextNavigationState.id)
            ?: throw Exception("Invalid navigation state")

        fragmentTransaction.attach(nextFragment)
        fragmentTransaction.commit()
    }

    internal fun shouldHandleScreenResult(): Boolean {
        return stateHandler.contains(SCREEN_RESULT)
    }

    internal fun setScreenResult(params: Any? = null) {
        stateHandler.save(SCREEN_RESULT, params)
    }

    internal fun getScreenResult(): Any? {
        return stateHandler.restore(SCREEN_RESULT)
    }

    internal fun canGoBack(): Boolean {
        return navigationStack.size > 1
    }

    companion object {
        private const val NAVIGATION_STACK = "navigation-stack"
        private const val SCREEN_RESULT = "screen-result"

        private var objectConverter: ObjectConverter? = null
        private var stateHandler: StateHandler? = null
        private val screenFactories: HashMap<String, ScreenFactory> = hashMapOf()

        fun registerObjectConverter(objectConverter: ObjectConverter) {
            this.objectConverter = objectConverter
        }

        fun resolveObjectConverter(): ObjectConverter {
            if (objectConverter == null) {
                val type = Class.forName("com.zulu.android.DefaultObjectConverter")
                objectConverter = type.newInstance() as ObjectConverter
            }

            objectConverter?.let {
                return it
            }

            throw NullPointerException()
        }

        fun registerScreenStateHandler(stateHandler: StateHandler) {
            this.stateHandler = stateHandler
        }

        fun resolveScreenStateHandler(): StateHandler {
            if (stateHandler == null) {
                val type = Class.forName("com.zulu.android.DefaultStateHandler")
                stateHandler = type.newInstance() as StateHandler
            }

            stateHandler?.let {
                return it
            }

            throw NullPointerException()
        }

        fun registerScreen(factory: ScreenFactory) {
            screenFactories[factory.name] = factory
        }

        fun <T : ScreenFactory> registerScreen(type: KClass<T>) {
            registerScreen(type.java.newInstance())
        }

        inline fun <reified T : ScreenFactory> registerScreen() {
            registerScreen(T::class)
        }

        fun resolveScreen(screenContext: ScreenContext): Screen {
            screenFactories[screenContext.name]?.let {
                return it.create(screenContext)
            }

            throw NullPointerException()
        }
    }
}