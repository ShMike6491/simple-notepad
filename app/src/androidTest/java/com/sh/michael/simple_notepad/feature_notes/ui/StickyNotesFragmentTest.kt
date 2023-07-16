package com.sh.michael.simple_notepad.feature_notes.ui

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.sh.michael.simple_notepad.R
import com.sh.michael.simple_notepad.feature_notes.domain.model.BackgroundColor
import com.sh.michael.simple_notepad.feature_notes.domain.model.IStickyNote
import com.sh.michael.simple_notepad.feature_notes.ui.model.StickyNoteState
import com.sh.michael.simple_notepad.onRecyclerViewItem
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest

@RunWith(AndroidJUnit4ClassRunner::class)
class StickyNotesFragmentTest : KoinTest {

    private val mockViewModel: StickyNotesViewModel = mockk(relaxed = true)

    @Before
    fun setup() {
        val module = module {
            viewModel { mockViewModel }
        }

        startKoin {
            modules(module)
        }
    }

    @After
    fun teardown() {
        stopKoin()
        clearMocks(mockViewModel)
    }

    @Test
    fun testRecyclerViewIsEmptyWhenStateIsEmpty() {
        every { mockViewModel.stateData } returns flowOf(emptyList())

        launchFragmentInContainer<StickyNotesFragment>(themeResId = R.style.Theme_Simplenotepad)

        onView(withId(R.id.notesRecycler))
            .check(matches(not(isDisplayed())))
    }

    @Test
    fun testNoteItemPresentsCorrectText() {
        val mockNote1: IStickyNote = mockk()
        val mockNote2: IStickyNote = mockk()
        val testState1 = StickyNoteState("testId1", mockNote1, null)
        val testState2 = StickyNoteState("testId2", mockNote2, null)

        every { mockNote1.noteText } returns "test first note text"
        every { mockNote1.backgroundColor } returns BackgroundColor.BLUE
        every { mockNote2.noteText } returns "test second note text"
        every { mockNote2.backgroundColor } returns BackgroundColor.BLUE
        every { mockViewModel.stateData } returns flowOf(listOf(testState1, testState2))

        launchFragmentInContainer<StickyNotesFragment>(themeResId = R.style.Theme_Simplenotepad)

        onView(withId(R.id.notesRecycler))
            .check(matches(isDisplayed()))

        onRecyclerViewItem(R.id.notesRecycler, 0, R.id.stickyNoteTextView)
            .check(matches(isDisplayed()))
            .check(matches(withText("test first note text")))

        onRecyclerViewItem(R.id.notesRecycler, 1, R.id.stickyNoteTextView)
            .check(matches(isDisplayed()))
            .check(matches(withText("test second note text")))
    }

    @Test
    fun testOnNoteItemSwipePrimaryActionIsCalled() {
        var actionHasBeenCalled = false

        val mockNote: IStickyNote = mockk()
        val testState = StickyNoteState("testId1", mockNote) { actionHasBeenCalled = true }

        every { mockNote.noteText } returns "test first note text"
        every { mockNote.backgroundColor } returns BackgroundColor.BLUE
        every { mockViewModel.stateData } returns flowOf(listOf(testState))

        launchFragmentInContainer<StickyNotesFragment>(themeResId = R.style.Theme_Simplenotepad)

        onRecyclerViewItem(R.id.notesRecycler, 0, R.id.stickyNoteTextView)
            .perform(swipeUp())

        assertEquals(true, actionHasBeenCalled)
    }
}