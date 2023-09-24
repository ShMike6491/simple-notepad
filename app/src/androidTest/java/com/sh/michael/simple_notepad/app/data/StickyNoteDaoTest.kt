package com.sh.michael.simple_notepad.app.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.sh.michael.simple_notepad.feature_notes.data.StickyNotesDao
import com.sh.michael.simple_notepad.feature_notes.data.model.RoomStickyNote
import com.sh.michael.simple_notepad.feature_notes.domain.model.BackgroundColor
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class StickyNoteDaoTest {

    lateinit var database: AppDatabase
    private val testDao: StickyNotesDao get() = database.stickyNotesDao()

    @Before
    fun setup() {
        val appContext = getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(appContext, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun testInsertAndGetAll() = runBlocking {
        val note = RoomStickyNote(
            id = "1",
            priority = 1,
            backgroundColor = BackgroundColor.BLUE,
            noteText = "Sample note"
        )
        testDao.insert(note)

        val notes = testDao.getAll().firstOrNull()
        assertEquals(1, notes?.size)
        assertEquals(note, notes?.get(0))
    }

    @Test
    fun testUpdateAndGetNoteById() = runBlocking {
        val note = RoomStickyNote(
            id = "1",
            priority = 1,
            backgroundColor = BackgroundColor.BLUE,
            noteText = "Sample note"
        )
        testDao.insert(note)

        val updatedNote = note.copy(noteText = "Updated note")
        testDao.update(updatedNote)

        val retrievedNote = testDao.getNoteById("1")
        assertEquals(updatedNote, retrievedNote)
    }

    @Test
    fun testDeleteAndGetAllByPriorityDesc() = runBlocking {
        val note1 = RoomStickyNote(
            id = "1",
            priority = 1,
            backgroundColor = BackgroundColor.BLUE,
            noteText = "Sample note 1"
        )
        val note2 = RoomStickyNote(
            id = "2",
            priority = 2,
            backgroundColor = BackgroundColor.GREEN,
            noteText = "Sample note 2"
        )
        testDao.insert(note1, note2)

        testDao.delete(note1)

        val notes = testDao.getAllByPriorityDesc().firstOrNull()
        assertEquals(1, notes?.size)
        assertEquals(note2, notes?.get(0))
    }


    @Test
    fun testInsertWithOnConflictIgnore() = runBlocking {
        val note1 = RoomStickyNote(
            id = "1",
            priority = 1,
            backgroundColor = BackgroundColor.BLUE,
            noteText = "Sample note 1"
        )
        val note2 = RoomStickyNote(
            id = "1",
            priority = 2,
            backgroundColor = BackgroundColor.GREEN,
            noteText = "Sample note 2"
        )

        testDao.insert(note1)
        testDao.insert(note2)

        val notes = testDao.getAll().firstOrNull()
        assertEquals(1, notes?.size)
        assertEquals(note1, notes?.get(0))
    }

    @Test
    fun testGetHighestPriority() = runBlocking {
        val note1 = RoomStickyNote(
            id = "1",
            priority = 1,
            backgroundColor = BackgroundColor.BLUE,
            noteText = "Sample note 1"
        )
        val note2 = RoomStickyNote(
            id = "2",
            priority = 2,
            backgroundColor = BackgroundColor.GREEN,
            noteText = "Sample note 2"
        )
        testDao.insert(note1, note2)

        val highestPriority = testDao.getHighestPriority()
        assertEquals(2, highestPriority)
    }
}