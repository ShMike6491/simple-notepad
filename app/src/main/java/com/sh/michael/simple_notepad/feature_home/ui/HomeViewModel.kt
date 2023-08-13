package com.sh.michael.simple_notepad.feature_home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sh.michael.simple_notepad.common.model.UiEvent
import com.sh.michael.simple_notepad.feature_home.ui.model.HomeState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.UUID

class HomeViewModel : ViewModel() {

    private val defaultState: HomeState = HomeState(
        id = UUID.randomUUID().toString(),
        onPrimaryAction = this::onPrimaryClick,
        onNoteAction = this::onNoteClick,
        onFileAction = this::onFileClick
    )

    private val eventChannel = Channel<UiEvent>()
    val uiEvent = eventChannel.receiveAsFlow()

    private val state = MutableStateFlow<HomeState>(defaultState)
    val stateData: StateFlow<HomeState> = state

    private fun onPrimaryClick() = viewModelScope.launch {
        switchExtendedState()
    }

    private fun onNoteClick() = viewModelScope.launch {
        switchExtendedState()
        eventChannel.send(
            UiEvent.Navigate(OPEN_NEW_NOTE_DIALOG)
        )
    }

    private fun onFileClick() = viewModelScope.launch {
        switchExtendedState()
        eventChannel.send(
            UiEvent.Navigate(OPEN_NEW_FILE_DIALOG)
        )
    }

    private fun switchExtendedState() = viewModelScope.launch {
        state.value = stateData.value.copy(
            isExtendedState = !stateData.value.isExtendedState
        )
    }

    companion object {
        const val OPEN_NEW_NOTE_DIALOG = "action_open_note_dialog"
        const val OPEN_NEW_FILE_DIALOG = "action_open_file_dialog"
    }
}