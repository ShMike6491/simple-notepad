package com.sh.michael.simple_notepad.feature_notes.ui

import com.sh.michael.simple_notepad.feature_notes.domain.IStickyNoteRepository
import com.sh.michael.simple_notepad.feature_notes.domain.model.BackgroundColor
import com.sh.michael.simple_notepad.feature_notes.domain.model.IStickyNote
import com.sh.michael.simple_notepad.feature_notes.ui.model.StickyNoteState
import io.mockk.clearMocks
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class StickyNotesViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private val mockRepo: IStickyNoteRepository = mockk()

    private lateinit var viewModel: StickyNotesViewModel

//    private val testFlow = MutableStateFlow<List<IStickyNote>>(emptyList())

    @Before
//    fun setup() {
//        every { mockRepo.observeAllNotes() } returns testFlow
//        viewModel = StickyNotesViewModel(mockRepo)
//    }
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = StickyNotesViewModel(mockRepo)
    }

    @After
    fun teardown() {
//        testFlow.value = emptyList()
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
        clearMocks(mockRepo)
    }

    @Test
    fun `example`() {
        every { mockRepo.observeAllNotes() } returns flowOf(emptyList())
        assertTrue(true)
    }

//    @Test
//    fun `test stateData returns expected list of StickyNoteState`() = runBlockingTest {
//        // Given
//        val stickyNote1 = mockk<IStickyNote>()
//        val stickyNote2 = mockk<IStickyNote>()
//        val stickyNotes = listOf(stickyNote1, stickyNote2)
//
//        every { mockRepo.observeAllNotes() } returns flowOf(stickyNotes)
//
//        // When
//        val stateData = viewModel.stateData
//
//        // Then
//        val expected = listOf(
//            StickyNoteState(stickyNote1.id, stickyNote1) { /* remove note callback */ },
//            StickyNoteState(stickyNote2.id, stickyNote2) { /* remove note callback */ }
//        )
//        val actual = stateData.first()
//        assertEquals(expected, actual)
//    }

//    @Test
//    fun `test stateData returns expected list of StickyNoteState`() = runBlocking {
//        // Given
//        val stickyNote1 = mockk<IStickyNote>()
//        val stickyNote2 = mockk<IStickyNote>()
//        val stickyNotes = listOf(stickyNote1, stickyNote2)
//        val stickyNoteStates = listOf(
//            StickyNoteState(stickyNote1.id, stickyNote1) { /* remove note callback */ },
//            StickyNoteState(stickyNote2.id, stickyNote2) { /* remove note callback */ }
//        )
//        every { repo.observeAllNotes() } returns flowOf(stickyNotes)
//
//        // When
//        val stateData = viewModel.stateData
//
//        // Then
//        val result = stateData.first()
//        assert(result == stickyNoteStates)
//    }
//
//    @Test
//    fun `test notesPositionChanged updates repository`() {
//        // Given
//        val fromPosition = 0
//        val toPosition = 1
//
//        // When
//        viewModel.notesPositionChanged(fromPosition, toPosition)
//
//        // Then
//        coVerify { repo.updateItemsPosition(fromPosition, toPosition) }
//    }
//
//    @Test
//    fun `test removeNote calls removeNoteById in repository`() = runBlocking {
//        // Given
//        val noteId = "note_id"
//
//        // When
//        viewModel.removeNote(noteId)
//
//        // Then
//        coVerify { repo.removeNoteById(noteId) }
//    }
}