package com.sh.michael.simple_notepad.feature_notes.ui.dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sh.michael.simple_notepad.common.interfaces.INavigationCallback
import com.sh.michael.simple_notepad.feature_notes.domain.IStickyNoteRepository
import com.sh.michael.simple_notepad.feature_notes.domain.model.BackgroundColor
import com.sh.michael.simple_notepad.feature_notes.ui.model.AddNoteState
import com.sh.michael.simple_notepad.feature_notes.ui.model.ColorOption
import com.sh.michael.simple_notepad.feature_notes.ui.model.asColorRes
import com.sh.michael.simple_notepad.feature_notes.ui.model.selectedColor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AddNoteViewModel(
    private val repo: IStickyNoteRepository,
    private val navigationCallback: INavigationCallback? // todo: migrate to UiEvent handling
) : ViewModel() {

    private val initialState = AddNoteState(
        colorSelectOptions = colorOptions(),
        titleChangeListener = this::onTitleChanged,
        onSubmitAction = this::submitNote,
        onCloseAction = this::exitDialog
    )

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
            exitDialog()
        }
    }

    private fun exitDialog() {
        navigationCallback?.onNavigate(DIALOG_DISMISS_ACTION)
    }

    companion object {
        const val MAX_NOTE_LENGTH = 50

        const val DIALOG_DISMISS_ACTION = "notes_dialog_dismiss_action"
    }
}