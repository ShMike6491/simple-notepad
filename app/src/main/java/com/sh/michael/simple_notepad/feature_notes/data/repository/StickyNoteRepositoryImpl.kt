package com.sh.michael.simple_notepad.feature_notes.data.repository

import com.sh.michael.simple_notepad.app.data.AppDatabase
import com.sh.michael.simple_notepad.feature_notes.data.model.RoomStickyNote
import com.sh.michael.simple_notepad.feature_notes.domain.IStickyNoteRepository
import com.sh.michael.simple_notepad.feature_notes.domain.model.BackgroundColor
import com.sh.michael.simple_notepad.feature_notes.domain.model.IStickyNote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID

class StickyNoteRepositoryImpl(
    private val database: AppDatabase
) : IStickyNoteRepository {

    private val notesDao = database.stickyNotesDao()

    override fun observeAllNotes(): Flow<List<IStickyNote>> = notesDao.getAllByPriorityDesc()

    override suspend fun removeNoteById(id: String) = notesDao.deleteById(id)

    override suspend fun updateItemsPosition(fromPosition: Int, toPosition: Int) {
        val currentNotes = notesDao.getAllByPriorityDesc().firstOrNull()

        val fromNote = currentNotes?.getOrNull(fromPosition) ?: return
        val toNote = currentNotes.getOrNull(toPosition) ?: return

        notesDao.run {
            update(fromNote.copy(priority = toNote.priority))
            update(toNote.copy(priority = fromNote.priority))
        }
    }

    override suspend fun createNote(noteText: String, color: BackgroundColor) {
        // fixme: eventually priority will get to MAX_INT_VALUE and we will need to deal with it
        val highestPriority = notesDao.getHighestPriority() ?: 0
        val newNote = RoomStickyNote(
            id = UUID.randomUUID().toString(),
            priority = highestPriority + 1,
            backgroundColor = color,
            noteText = noteText
        )
        notesDao.insert(newNote)
    }

    override suspend fun insertNote(note: IStickyNote) {
        val newNote = RoomStickyNote(
            id = note.id,
            priority = note.priority,
            backgroundColor = note.backgroundColor,
            noteText = note.noteText
        )

        notesDao.insert(newNote)
    }
}