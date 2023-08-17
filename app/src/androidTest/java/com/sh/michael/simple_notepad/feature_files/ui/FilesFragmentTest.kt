package com.sh.michael.simple_notepad.feature_files.ui

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isSelected
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.sh.michael.simple_notepad.R
import com.sh.michael.simple_notepad.common.model.UiError
import com.sh.michael.simple_notepad.common.model.UiString.*
import com.sh.michael.simple_notepad.feature_files.ui.model.FileState
import com.sh.michael.simple_notepad.feature_pages.ui.PagesViewModel
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
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

@RunWith(AndroidJUnit4ClassRunner::class)
class FilesFragmentTest {

    private val mockViewModel: FilesViewModel = mockk(relaxed = true)
    private val mockPages: PagesViewModel = mockk(relaxed = true)

    @Before
    fun setup() {
        val module = module {
            viewModel { mockViewModel }
            viewModel { mockPages }
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
    fun testEmptyState() {
        val testState = UiError(R.drawable.image_question, DynamicString("test title"), DynamicString("test message"))

        every { mockViewModel.defaultEmptyState } returns testState
        every { mockViewModel.uiEvent } returns emptyFlow()
        every { mockViewModel.stateData } returns MutableStateFlow(emptyList())

        launchFragmentInContainer<FilesFragment>(themeResId = R.style.Theme_Simplenotepad)

        onView(withId(R.id.emptyStateContainer))
            .check(matches(isDisplayed()))

        onView(withId(R.id.errorImageView))
            .check(matches(isDisplayed()))

        onView(withId(R.id.errorTitleTextView))
            .check(matches(withText("test title")))

        onView(withId(R.id.errorMessageTextView))
            .check(matches(withText("test message")))
    }

    @Test
    fun testFileTitle() {
        val testData = listOf(FileState("test", DynamicString("test title")))

        every { mockViewModel.defaultEmptyState } returns UiError()
        every { mockViewModel.uiEvent } returns emptyFlow()
        every { mockViewModel.stateData } returns MutableStateFlow(testData)

        launchFragmentInContainer<FilesFragment>(themeResId = R.style.Theme_Simplenotepad)

        onView(withId(R.id.emptyStateContainer))
            .check(matches(not(isDisplayed())))

        onView(withText("test title"))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testSelectedTabGetsSelected() {
        val testData = listOf(
            FileState("test1", DynamicString("title 1")),
            FileState("test2", DynamicString("title 2")),
            FileState("test3", DynamicString("title 3")),
        )

        every { mockViewModel.defaultEmptyState } returns UiError()
        every { mockViewModel.uiEvent } returns emptyFlow()
        every { mockViewModel.stateData } returns MutableStateFlow(testData)

        launchFragmentInContainer<FilesFragment>(themeResId = R.style.Theme_Simplenotepad)

        onView(withText("title 2"))
            .check(matches(not(isSelected())))
            .perform(click())
            .check(matches(isSelected()))
    }
}