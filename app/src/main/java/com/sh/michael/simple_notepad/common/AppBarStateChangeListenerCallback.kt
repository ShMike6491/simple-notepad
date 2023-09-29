package com.sh.michael.simple_notepad.common

import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener

/**
 * App bar collapsing state
 * @author Paulo Caldeira <paulo.caldeira></paulo.caldeira>@acin.pt>.
 */
abstract class AppBarStateChangeListener : OnOffsetChangedListener {

    private var currentState = State.IDLE

    /**
     * Notifies on state change
     * @param appBarLayout Layout
     * @param state Collapse state
     */
    abstract fun onStateChanged(appBarLayout: AppBarLayout?, state: State)

    override fun onOffsetChanged(appBarLayout: AppBarLayout, i: Int) {
        when {
            (i == 0) -> updateAppbarState(appBarLayout, State.EXPANDED)
            (Math.abs(i) >= appBarLayout.totalScrollRange) -> updateAppbarState(appBarLayout, State.COLLAPSED)
            else -> updateAppbarState(appBarLayout, State.IDLE)
        }
    }

    private fun updateAppbarState(appBarLayout: AppBarLayout, state: State) {
        if (currentState != state) onStateChanged(appBarLayout, state)
        currentState = state
    }

    enum class State { EXPANDED, COLLAPSED, IDLE }
}