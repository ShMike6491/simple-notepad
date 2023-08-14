package com.sh.michael.simple_notepad.feature_notes.domain

import com.sh.michael.simple_notepad.feature_notes.domain.model.BackgroundColor
import com.sh.michael.simple_notepad.feature_notes.domain.model.IStickyNote
import kotlinx.coroutines.flow.Flow

interface IStickyNoteRepository {

    fun observeAllNotes(): Flow<List<IStickyNote>>

    suspend fun removeNoteById(id: String)

    suspend fun updateItemsPosition(fromPosition: Int, toPosition: Int)

    suspend fun createNote(noteText: String, color: BackgroundColor)

    suspend fun insertNote(note: IStickyNote)
}