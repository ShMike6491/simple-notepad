package com.sh.michael.simple_notepad.feature_menu.ui

import com.sh.michael.simple_notepad.common.model.UiEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SideMenuViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()

    private lateinit var viewModel: SideMenuViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = SideMenuViewModel()
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `test email state initialized correctly`() {
        val state = viewModel.emailState

        assertEquals(true, state.onItemClickAction != null)
        assertEquals(true, state.menuTitle != null)
        assertEquals(true, state.hasIcon)
        assertEquals("menuItem", state.id)
    }

    @Test
    fun `test exit click event flow`(): Unit = runBlocking {
        viewModel.onExitClick()

        val event = viewModel.uiEvent.first()
        assertEquals(UiEvent.PopBackStack, event)
    }

    @Test
    fun `test email click event flow`(): Unit = runBlocking {
        viewModel.emailState
            .onItemClickAction
            ?.invoke()

        val expected = UiEvent.Navigate("action_menu_navigate_to_contacts")
        val actual = viewModel.uiEvent.first()
        assertEquals(expected, actual)
    }
}