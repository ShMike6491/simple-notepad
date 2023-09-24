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
        val state = viewModel.menuState

        assertEquals(2, state.size)
        assertEquals("menuItem", state.first().id)
        assertEquals("privacyItem", state[1].id)
    }

    @Test
    fun `test exit click event flow`(): Unit = runBlocking {
        viewModel.onExitClick()

        val event = viewModel.uiEvent.first()
        assertEquals(UiEvent.PopBackStack, event)
    }

    @Test
    fun `test email click event flow`(): Unit = runBlocking {
        viewModel.menuState
            .first()
            .onItemClickAction
            ?.invoke()

        val expected = UiEvent.Navigate("action_menu_navigate_to_contacts")
        val actual = viewModel.uiEvent.first()
        assertEquals(expected, actual)
    }

    @Test
    fun `test privacy click event flow`(): Unit = runBlocking {
        viewModel.menuState
            .get(1)
            .onItemClickAction
            ?.invoke()

        val expected = UiEvent.Web("https://doc-hosting.flycricket.io/inkit-privacy-policy/7f213bbd-a7dd-448a-b99b-1f9060c15560/privacy")
        val actual = viewModel.uiEvent.first()
        assertEquals(expected, actual)
    }
}