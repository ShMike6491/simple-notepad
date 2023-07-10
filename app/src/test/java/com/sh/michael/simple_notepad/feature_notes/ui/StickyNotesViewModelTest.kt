package com.sh.michael.simple_notepad.feature_notes.ui

import com.sh.michael.simple_notepad.feature_notes.domain.IStickyNoteRepository
import com.sh.michael.simple_notepad.feature_notes.domain.model.IStickyNote
import com.sh.michael.simple_notepad.feature_notes.ui.model.StickyNoteState
import io.mockk.clearMocks
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class StickyNotesViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private val mockRepo: IStickyNoteRepository = mockk()

    private lateinit var viewModel: StickyNotesViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = StickyNotesViewModel(mockRepo)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
        clearMocks(mockRepo)
    }

    @Test
    fun `test stateData returns expected list of StickyNoteState`() = runBlockingTest {
        // Given
        val stickyNote1 = mockk<IStickyNote>()
        val stickyNote2 = mockk<IStickyNote>()
        val stickyNotes = listOf(stickyNote1, stickyNote2)

        every { mockRepo.observeAllNotes() } returns flowOf(stickyNotes)
        every { stickyNote1.id } returns "test1"
        every { stickyNote2.id } returns "test2"

        // When
        val stateData = viewModel.stateData

        // Then
        val expected = arrayListOf(
            StickyNoteState("test1", stickyNote1) { /* remove note callback */ },
            StickyNoteState("test2", stickyNote2) { /* remove note callback */ }
        )
        val actual = stateData.first()

        assertEquals(expected.first().note, actual.first().note)
        assertEquals(expected.last().note, actual.last().note)
    }

    @Test
    fun `test notesPositionChanged updates repository`() {
        // Given
        val fromPosition = 0
        val toPosition = 1

        // When
        viewModel.notesPositionChanged(fromPosition, toPosition)

        // Then
        coVerify { mockRepo.updateItemsPosition(fromPosition, toPosition) }
    }

    @Test
    fun `test removeNote calls removeNoteById in repository`() = runBlocking {
        // Given
        val stickyNote = mockk<IStickyNote>()

        every { stickyNote.id } returns "note_id"
        every { mockRepo.observeAllNotes() } returns flowOf(listOf(stickyNote))

        // When
        viewModel.stateData
            .first()
            .first()
            .onPrimaryAction!!
            .invoke()

        // Then
        coVerify { mockRepo.removeNoteById("note_id") }
    }
}