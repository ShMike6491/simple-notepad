package com.sh.michael.simple_notepad.feature_home.ui

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.sh.michael.simple_notepad.R
import com.sh.michael.simple_notepad.feature_files.ui.FilesViewModel
import com.sh.michael.simple_notepad.feature_home.ui.model.HomeState
import com.sh.michael.simple_notepad.feature_notes.ui.StickyNotesViewModel
import com.sh.michael.simple_notepad.feature_pages.ui.PagesViewModel
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.not
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
import java.util.UUID

@RunWith(AndroidJUnit4ClassRunner::class)
class HomeFragmentTest : KoinTest {

    private val mockViewModel: HomeViewModel = mockk(relaxed = true)
    private val mockPages: PagesViewModel = mockk(relaxed = true)
    private val mockFiles: FilesViewModel = mockk(relaxed = true)
    private val mockNotes: StickyNotesViewModel = mockk(relaxed = true)

    private val mockState = HomeState(UUID.randomUUID().toString())

    @Before
    fun setup() {
        val module = module {
            viewModel { mockViewModel }
            viewModel { mockPages }
            viewModel { mockFiles }
            viewModel { mockNotes }
        }

        startKoin {
            modules(module)
        }
    }

    @After
    fun teardown() {
        stopKoin()
        clearMocks(
            mockViewModel,
            mockPages,
            mockFiles,
            mockNotes
        )
    }

    @Test
    fun testNormalState(): Unit = runBlocking {
        every { mockViewModel.stateData } returns MutableStateFlow(mockState)
        every { mockViewModel.uiEvent } returns emptyFlow()

        launchFragmentInContainer<HomeFragment>(themeResId = R.style.Theme_Simplenotepad)

        delay(200)

        onView(withId(R.id.addNewFab))
            .check(matches(isDisplayed()))

        onView(withId(R.id.newFileFab))
            .check(matches(not(isDisplayed())))

        onView(withId(R.id.newNoteFab))
            .check(matches(not(isDisplayed())))

        onView(withId(R.id.dimmedBackgroundView))
            .check(matches(not(isDisplayed())))
    }

    @Test
    fun testExtendedState() {
        val state = mockState.copy(isExtendedState = true)

        every { mockViewModel.stateData } returns MutableStateFlow(state)
        every { mockViewModel.uiEvent } returns emptyFlow()

        launchFragmentInContainer<HomeFragment>(themeResId = R.style.Theme_Simplenotepad)

        onView(withId(R.id.addNewFab))
            .check(matches(isDisplayed()))

        onView(withId(R.id.newFileFab))
            .check(matches(isDisplayed()))

        onView(withId(R.id.newNoteFab))
            .check(matches(isDisplayed()))

        onView(withId(R.id.dimmedBackgroundView))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testPrimaryActionClick() {
        var actionHasBeenClicked = false
        val state = mockState.copy(
            isExtendedState = true,
            onPrimaryAction = { actionHasBeenClicked = true }
        )

        every { mockViewModel.stateData } returns MutableStateFlow(state)
        every { mockViewModel.uiEvent } returns emptyFlow()

        launchFragmentInContainer<HomeFragment>(themeResId = R.style.Theme_Simplenotepad)

        onView(withId(R.id.addNewFab))
            .check(matches(isDisplayed()))
            .perform(click())

        assertEquals(true, actionHasBeenClicked)
    }

    @Test
    fun testNoteActionClick() {
        var actionHasBeenClicked = false
        val state = mockState.copy(
            isExtendedState = true,
            onNoteAction = { actionHasBeenClicked = true }
        )

        every { mockViewModel.stateData } returns MutableStateFlow(state)
        every { mockViewModel.uiEvent } returns emptyFlow()

        launchFragmentInContainer<HomeFragment>(themeResId = R.style.Theme_Simplenotepad)

        onView(withId(R.id.newNoteFab))
            .check(matches(isDisplayed()))
            .perform(click())

        assertEquals(true, actionHasBeenClicked)
    }

    @Test
    fun testFileActionClick() {
        var actionHasBeenClicked = false
        val state = mockState.copy(
            isExtendedState = true,
            onFileAction = { actionHasBeenClicked = true }
        )

        every { mockViewModel.stateData } returns MutableStateFlow(state)
        every { mockViewModel.uiEvent } returns emptyFlow()

        launchFragmentInContainer<HomeFragment>(themeResId = R.style.Theme_Simplenotepad)

        onView(withId(R.id.newFileFab))
            .check(matches(isDisplayed()))
            .perform(click())

        assertEquals(true, actionHasBeenClicked)
    }
}