package com.sh.michael.simple_notepad.feature_notes.data.repository

import com.sh.michael.simple_notepad.app.data.AppDatabase
import com.sh.michael.simple_notepad.feature_notes.data.StickyNotesDao
import com.sh.michael.simple_notepad.feature_notes.data.model.RoomStickyNote
import com.sh.michael.simple_notepad.feature_notes.domain.model.BackgroundColor
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.UUID
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class StickyNoteRepositoryImplTest {

    private val mockDatabase: AppDatabase = mockk()
    private val mockDao: StickyNotesDao = mockk(relaxed = true)

    private lateinit var repository: StickyNoteRepositoryImpl

    @Before
    fun setup() {
        every { mockDatabase.stickyNotesDao() } returns mockDao
        repository = StickyNoteRepositoryImpl(mockDatabase)
    }

    @After
    fun teardown() {
        clearMocks(mockDatabase, mockDao)
    }

    @Test
    fun `observeAllNotes returns flow of notes`() = runBlockingTest {
        val mockNote1: RoomStickyNote = mockk()
        val mockNote2: RoomStickyNote = mockk()
        val mockNotesList = listOf(mockNote1, mockNote2)
        every { mockDao.getAllByPriorityDesc() } returns flowOf(mockNotesList)

        val flowResult = repository.observeAllNotes().first()

        assertEquals(mockNotesList, flowResult)
        coVerify { mockDao.getAllByPriorityDesc() }
    }

    @Test
    fun `removeNoteById calls deleteById on notesDao`() = runBlockingTest {
        val noteId = UUID.randomUUID().toString()

        repository.removeNoteById(noteId)

        coVerify { mockDao.deleteById(noteId) }
    }

    @Test
    fun `updateItemsPosition swaps note positions`() = runBlockingTest {
        val fromPosition = 1
        val toPosition = 2

        val baseMockedNote = RoomStickyNote("", -1, BackgroundColor.BLUE, "Note text")
        val mockNote1: RoomStickyNote = baseMockedNote.copy(id = "1", priority = 1)
        val mockNote2: RoomStickyNote = baseMockedNote.copy(id = "2", priority = 2)
        val mockNote3: RoomStickyNote = baseMockedNote.copy(id = "3", priority = 3)
        val mockNotesList = listOf(mockNote1, mockNote2, mockNote3)

        every { mockDao.getAllByPriorityDesc() } returns flowOf(mockNotesList)

        repository.updateItemsPosition(fromPosition, toPosition)

        // Assert that the note positions have been correctly updated in the mockNotesList
        // Verify that the update function was called on notesDao with the correct parameters
        coVerify {
            mockDao.update(match<RoomStickyNote> { note ->
                note.id == "3" && note.priority == 2
            })
            mockDao.update(match<RoomStickyNote> { note ->
                note.id == "2" && note.priority == 3
            })
        }
    }

    @Test
    fun `createNote inserts a new note`() = runBlockingTest {
        val noteText = "Sample note"
        val color = BackgroundColor.BLUE
        val highestPriority = 5
        val mockNote = RoomStickyNote(
            id = UUID.randomUUID().toString(),
            priority = highestPriority + 1,
            backgroundColor = color,
            noteText = noteText
        )
        coEvery { mockDao.getHighestPriority() } returns highestPriority

        repository.createNote(noteText, color)

        // Verify that the insert function was called on notesDao with the correct parameters
        coVerify {
            mockDao.insert(match<RoomStickyNote> { note ->
                    note.priority == mockNote.priority &&
                    note.backgroundColor == mockNote.backgroundColor &&
                    note.noteText == mockNote.noteText
            })
        }
    }

    @Test
    fun `createNote inserts a new note with priority 1 when first note created`() = runBlockingTest {
        val noteText = "Sample note"
        val color = BackgroundColor.BLUE
        val mockNote = RoomStickyNote(
            id = UUID.randomUUID().toString(),
            priority = -1,
            backgroundColor = color,
            noteText = noteText
        )

        coEvery { mockDao.getHighestPriority() } returns null

        repository.createNote(noteText, color)

        // Verify that the insert function was called on notesDao with the correct parameters
        coVerify {
            mockDao.insert(match<RoomStickyNote> { note ->
                    note.priority == 1 &&
                    note.backgroundColor == mockNote.backgroundColor &&
                    note.noteText == mockNote.noteText
            })
        }
    }

    @Test
    fun `insert updated notes list changes priority`() = runBlockingTest {
        val mockNote1 = RoomStickyNote(
            id = "test1",
            priority = 2,
            backgroundColor = BackgroundColor.BLUE,
            noteText = "note1"
        )

        val mockNote2 = RoomStickyNote(
            id = "test2",
            priority = 9,
            backgroundColor = BackgroundColor.BLUE,
            noteText = "note2"
        )

        coEvery { mockDao.update(any<List<RoomStickyNote>>()) } returns Unit

        repository.updateItems(listOf(mockNote1, mockNote2))

        coVerify {
            mockDao.update(
                match<List<RoomStickyNote>> { notes ->
                    notes[0].priority == 2
                        && notes[1].priority == 1
                        && notes[0].id == "test1"
                        && notes[1].id == "test2"
                }
            )
        }
    }
}
