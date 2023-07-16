package com.sh.michael.simple_notepad.feature_notes.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sh.michael.simple_notepad.feature_notes.domain.IStickyNoteRepository
import com.sh.michael.simple_notepad.feature_notes.domain.model.BackgroundColor
import com.sh.michael.simple_notepad.feature_notes.domain.model.IStickyNote
import com.sh.michael.simple_notepad.feature_notes.ui.model.StickyNoteState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class StickyNotesViewModel(
    private val repo: IStickyNoteRepository
) : ViewModel() {

    val stateData: Flow<List<StickyNoteState>> get() = repo.observeAllNotes()
        .map { it.mapAsStateData() }

    fun notesPositionChanged(fromPosition: Int, toPosition: Int) = viewModelScope.launch {
        repo.updateItemsPosition(fromPosition, toPosition)
    }

    private fun removeNote(noteId: String) = viewModelScope.launch {
        repo.removeNoteById(noteId)
    }

    private fun List<IStickyNote>.mapAsStateData(): List<StickyNoteState> {
        return this.map { item ->
            StickyNoteState(item.id, item) {
                this@StickyNotesViewModel.removeNote(item.id)
            }
        }
    }
}