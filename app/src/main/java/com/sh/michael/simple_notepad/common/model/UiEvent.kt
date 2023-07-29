package com.sh.michael.simple_notepad.common.model

sealed class UiEvent {
    object PopBackStack: UiEvent()
    data class Navigate(val route: String): UiEvent()
    data class ShowSnackbar(val state: SnackBarState): UiEvent()
}