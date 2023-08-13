package com.sh.michael.simple_notepad.feature_home.ui

import com.sh.michael.simple_notepad.common.model.UiEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class HomeViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = HomeViewModel()
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `test default state`() {
        val state = viewModel.stateData.value

        assertEquals(false, state.isExtendedState)
        assertEquals(true, state.onFileAction != null)
        assertEquals(true, state.onNoteAction != null)
        assertEquals(true, state.onPrimaryAction != null)
    }

    @Test
    fun `test primary click will switch state`() {
        val state = viewModel.stateData

        assertEquals(false, state.value.isExtendedState)

        state.value.onPrimaryAction!!.invoke()

        assertEquals(true, state.value.isExtendedState)
    }

    @Test
    fun `test on new note click event triggered`() = runBlocking {
        viewModel.stateData
            .value
            .onNoteAction
            ?.invoke()

        val expected = UiEvent.Navigate("action_open_note_dialog")
        val actual = viewModel.uiEvent.first()

        assertEquals(expected, actual)
    }

    @Test
    fun `test on new file click event triggered`() = runBlocking {
        viewModel.stateData
            .value
            .onFileAction
            ?.invoke()

        val expected = UiEvent.Navigate("action_open_file_dialog")
        val actual = viewModel.uiEvent.first()

        assertEquals(expected, actual)
    }
}