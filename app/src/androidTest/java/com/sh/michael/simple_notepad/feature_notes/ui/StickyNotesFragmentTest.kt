package com.sh.michael.simple_notepad.feature_notes.ui

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.runner.AndroidJUnit4
import com.sh.michael.simple_notepad.R
import com.sh.michael.simple_notepad.feature_notes.domain.model.IStickyNote
import com.sh.michael.simple_notepad.feature_notes.ui.model.StickyNoteState
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.test.KoinTest

@RunWith(AndroidJUnit4ClassRunner::class)
//@RunWith(AndroidJUnit4::class)
class StickyNotesFragmentTest : KoinTest {

    private val mockViewModel: StickyNotesViewModel = mockk()
    private val mockNote: IStickyNote = mockk(relaxed = true)

//    private val testFlow = MutableStateFlow<List<StickyNoteState>>(emptyList())

    @Before
    fun setup() {
//        Intents.init()

        val module = module {
            viewModel { mockViewModel }
        }

        startKoin {
            modules(module)
        }

//        every { mockViewModel.stateData } returns testFlow
    }

    @After
    fun teardown() {
//        testFlow.value = emptyList()
        clearMocks(mockViewModel)
    }

//    @Test
//    fun correctNoteTextIsShown() {
//        testFlow.value = listOf(testState)
//
//        every { mockNote.noteText } returns "test note text"
//        launchFragmentInContainer<StickyNotesFragment>(themeResId = R.style.Theme_Simplenotepad)
//
//        onRecyclerViewItem(R.id.notesRecycler, 0, R.id.stickyNoteTextView)
//            .check(matches(isDisplayed()))
//            .check(matches(withText("test note text")))
//    }

    @Test
    fun sampleTest() {
        every { mockViewModel.stateData } returns flowOf(emptyList())
//        FragmentScenario.launchInContainer(StickyNotesFragment::class.java)
        launchFragmentInContainer<StickyNotesFragment>(themeResId = R.style.Theme_Simplenotepad)

        assertTrue(true)
    }

    private val testState = StickyNoteState("", mockNote,)
}