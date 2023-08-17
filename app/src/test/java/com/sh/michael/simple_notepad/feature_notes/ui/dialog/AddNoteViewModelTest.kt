package com.sh.michael.simple_notepad.feature_notes.ui.dialog

import com.sh.michael.simple_notepad.common.model.UiEvent
import com.sh.michael.simple_notepad.feature_notes.domain.IStickyNoteRepository
import com.sh.michael.simple_notepad.feature_notes.domain.model.BackgroundColor
import com.sh.michael.simple_notepad.feature_notes.ui.model.asColorRes
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AddNoteViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private val mockRepo: IStickyNoteRepository = mockk(relaxed = true)
    private lateinit var viewModel: AddNoteViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = AddNoteViewModel(mockRepo)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
        clearMocks(mockRepo)
    }

    @Test
    fun `test initial state data`() {
        val actualState = viewModel.pageState.value

        val expectedColor = BackgroundColor.values().drop(1).first().asColorRes
        val selectedColor = actualState.colorSelectOptions.filter { it.isColorSelected }

        assertEquals("", actualState.titleValue)
        assertEquals(1, selectedColor.size)
        assertEquals(expectedColor, selectedColor.first().backgroundColor)
        assertEquals(true, actualState.onCloseAction != null)
        assertEquals(true, actualState.onSubmitAction != null)
        assertEquals(true, actualState.titleChangeListener != null)
    }

    @Test
    fun `test note text change updates state data`() {
        viewModel.pageState
            .value
            .titleChangeListener?.invoke("this is a test")

        val expected = "this is a test"
        val actual = viewModel.pageState
            .value
            .titleValue

        assertEquals(expected, actual)
    }

    @Test
    fun `background color click listener updates state data selected color option`() {
        val indexSelected = 3

        viewModel.pageState
            .value
            .colorSelectOptions
            .get(indexSelected)
            .onSelectAction
            ?.invoke()

        val expectedColor = BackgroundColor.values().drop(1).get(indexSelected)
        val actualColor = viewModel.pageState
            .value
            .colorSelectOptions
            .filter { it.isColorSelected }

        assertEquals(1, actualColor.size)
        assertEquals(expectedColor.asColorRes, actualColor.first().backgroundColor)
    }

    @Test
    fun `close action will trigger popup ui event flow update`() = runBlocking {
        viewModel.pageState
            .value
            .onCloseAction
            ?.invoke()

        val event = viewModel.uiEvent.first()

        assertEquals(UiEvent.PopBackStack, event)
    }

    @Test
    fun `empty title submit action will trigger submit ui event flow update`() = runBlocking {
        coEvery { mockRepo.createNote(any(), any()) } returns Unit

        viewModel.pageState
            .value
            .onSubmitAction
            ?.invoke()

        val expectedColor = BackgroundColor.values().drop(1).first()
        val firstEvent = viewModel.uiEvent.first()
        val secondEvent = viewModel.uiEvent.first()

        assertEquals(true, firstEvent is UiEvent.ShowSnackbar)
        assertEquals(UiEvent.PopBackStack, secondEvent)

        coVerify(exactly = 1) {
            mockRepo.createNote("", expectedColor)
        }
    }

    @Test
    fun `submit will save note and trigger submit ui event flow update`() = runBlocking {
        coEvery { mockRepo.createNote(any(), any()) } returns Unit

        val state = viewModel.pageState.value

        state.titleChangeListener?.invoke("test title")

        state.onSubmitAction?.invoke()

        val expectedColor = BackgroundColor.values().drop(1).first()
        val firstEvent = viewModel.uiEvent.first()
        val secondEvent = viewModel.uiEvent.first()

        assertEquals(true, firstEvent is UiEvent.ShowSnackbar)
        assertEquals(UiEvent.PopBackStack, secondEvent)

        coVerify(exactly = 1) {
            mockRepo.createNote("test title", expectedColor)
        }
    }
}