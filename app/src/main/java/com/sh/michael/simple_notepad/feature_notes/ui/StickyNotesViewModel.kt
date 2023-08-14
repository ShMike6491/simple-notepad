package com.sh.michael.simple_notepad.feature_notes.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sh.michael.simple_notepad.R
import com.sh.michael.simple_notepad.common.model.DEFAULT_SNACKBAR
import com.sh.michael.simple_notepad.common.model.UiEvent
import com.sh.michael.simple_notepad.common.model.UiString.*
import com.sh.michael.simple_notepad.feature_notes.domain.IStickyNoteRepository
import com.sh.michael.simple_notepad.feature_notes.domain.model.IStickyNote
import com.sh.michael.simple_notepad.feature_notes.ui.model.StickyNoteState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicReference

class StickyNotesViewModel(
    private val repo: IStickyNoteRepository
) : ViewModel() {

    private val noteToRemove = AtomicReference<IStickyNote?>(null)

    private val eventChannel = Channel<UiEvent>()
    val uiEvent = eventChannel.receiveAsFlow()

    val stateData: Flow<List<StickyNoteState>> get() = repo.observeAllNotes()
        .map { it.mapAsStateData() }

    fun notesPositionChanged(fromPosition: Int, toPosition: Int) = viewModelScope.launch {
        repo.updateItemsPosition(fromPosition, toPosition)
    }

    private fun removeNote(noteId: String) = viewModelScope.launch {
        val note = stateData.first()
            .find { it.id == noteId }
            .also { noteToRemove.set(it?.note) }
            ?: return@launch

        showUndoRemoveDialog()

        repo.removeNoteById(note.id)
    }

    private fun showUndoRemoveDialog() = viewModelScope.launch {
        val undoSnackbar = DEFAULT_SNACKBAR.copy(
            title = StringResource(R.string.removed_note_message),
            buttonText = StringResource(R.string.undo_caps),
            onActionClick = this@StickyNotesViewModel::undoNoteRemoval
        )

        eventChannel.send(
            UiEvent.ShowSnackbar(undoSnackbar)
        )
    }

    private fun undoNoteRemoval() = viewModelScope.launch {
        noteToRemove.getAndSet(null)?.let {
            repo.insertNote(it)
        }
    }

    private fun List<IStickyNote>.mapAsStateData(): List<StickyNoteState> {
        return this.map { item ->
            StickyNoteState(item.id, item) {
                this@StickyNotesViewModel.removeNote(item.id)
            }
        }
    }
}