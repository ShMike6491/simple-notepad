package com.sh.michael.simple_notepad.feature_notes.ui.dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sh.michael.simple_notepad.R
import com.sh.michael.simple_notepad.common.model.SUCCESS_SNACKBAR
import com.sh.michael.simple_notepad.common.model.UiEvent
import com.sh.michael.simple_notepad.common.model.UiString
import com.sh.michael.simple_notepad.feature_notes.domain.IStickyNoteRepository
import com.sh.michael.simple_notepad.feature_notes.domain.model.BackgroundColor
import com.sh.michael.simple_notepad.feature_notes.ui.model.AddNoteState
import com.sh.michael.simple_notepad.feature_notes.ui.model.ColorOption
import com.sh.michael.simple_notepad.feature_notes.ui.model.asColorRes
import com.sh.michael.simple_notepad.feature_notes.ui.model.selectedColor
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddNoteViewModel(
    private val repo: IStickyNoteRepository
) : ViewModel() {

    private val initialState = AddNoteState(
        colorSelectOptions = colorOptions(),
        titleChangeListener = this::onTitleChanged,
        onSubmitAction = this::submitNote,
        onCloseAction = this::exitDialog
    )

    private val eventChannel = Channel<UiEvent>()
    val uiEvent = eventChannel.receiveAsFlow()

    private val pageStateFlow = MutableStateFlow(initialState)
    val pageState: StateFlow<AddNoteState> = pageStateFlow

    private fun colorOptions(): List<ColorOption> {
        val availableOptions = BackgroundColor.values().drop(1)

        return availableOptions.mapIndexed { index, item ->
            ColorOption(
                isColorSelected = index == 0,
                colorValue = item,
                backgroundColor = item.asColorRes,
                onSelectAction = { onSelectedColorChanged(item) }
            )
        }
    }

    private fun onTitleChanged(value: CharSequence?) {
        val titleIsNotValid = value?.toString()?.run {
            pageState.value.titleValue == this || this.length > MAX_NOTE_LENGTH
        }

        if (titleIsNotValid == true) return

        pageStateFlow.value = pageState.value.copy(
            titleValue = value?.toString() ?: ""
        )
    }

    private fun onSelectedColorChanged(value: BackgroundColor) {
        if (pageState.value.selectedColor?.colorValue == value) return

        val updatedOptions: List<ColorOption> = pageState.value.colorSelectOptions.map {
            it.copy(isColorSelected = it.colorValue == value)
        }

        pageStateFlow.value = pageState.value.copy(
            colorSelectOptions = updatedOptions
        )
    }

    private fun submitNote() = viewModelScope.launch {
        val note = pageState.value
        val selectedColor = note.selectedColor?.colorValue ?: BackgroundColor.UNDEFINED

        repo.createNote(
            noteText = note.titleValue,
            color = selectedColor
        ).runCatching {
            showSuccess()
            exitDialog()
        }
    }

    private fun exitDialog() = viewModelScope.launch {
        eventChannel.send(UiEvent.PopBackStack)
    }

    private fun showSuccess() = viewModelScope.launch {
        val snackbar = SUCCESS_SNACKBAR.copy(
            title = UiString.StringResource(R.string.new_note_was_created)
        )

        eventChannel.send(
            UiEvent.ShowSnackbar(snackbar)
        )
    }

    companion object {

        const val MAX_NOTE_LENGTH = 50
    }
}