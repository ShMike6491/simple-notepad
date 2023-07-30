package com.sh.michael.simple_notepad.common.model

/**
 * This is a better approach for event handling, when compared to [INavigationCallback]
 * This event can be passed from view model to update fragment navigation or explicit messages presentation.
 *
 * Should be emitted in a coroutine's [Channel] or [SharedFlow] and collected with [Collect] to avoid event
 * duplication in case of activity recreation.
 */
sealed class UiEvent {
    object PopBackStack: UiEvent()
    data class Navigate(val route: String): UiEvent()
    data class ShowSnackbar(val state: SnackBarState): UiEvent()
}