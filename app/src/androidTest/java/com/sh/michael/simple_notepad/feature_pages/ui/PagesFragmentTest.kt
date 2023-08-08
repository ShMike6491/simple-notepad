package com.sh.michael.simple_notepad.feature_pages.ui

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withHint
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.sh.michael.simple_notepad.R
import com.sh.michael.simple_notepad.common.model.UiError
import com.sh.michael.simple_notepad.common.model.UiEvent
import com.sh.michael.simple_notepad.common.model.UiString.*
import com.sh.michael.simple_notepad.feature_pages.ui.model.PageState
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
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
class PagesFragmentTest {

    private val mockViewModel: PagesViewModel = mockk(relaxed = true)
    private val mockEvent: UiEvent = mockk()
    private val mockState: PageState = mockk()

    // todo: add delete icon tests

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
        clearMocks(
            mockViewModel,
            mockEvent,
            mockState
        )
    }

    @Test
    fun testInitialState() {
        launchFragmentInContainer<PagesFragment>()

        onView(withId(R.id.mainEditText))
            .check(matches(isDisplayed()))
            .check(matches(isEnabled()))

        onView(withId(R.id.errorContainer))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))

        onView(withId(R.id.loadingBar))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
    }

    @Test
    fun testErrorState() {
        val errorState = UiError(
            errorImage = R.drawable.image_error_monster,
            titleText = DynamicString("Error Title"),
            messageText = DynamicString("This is an error message")
        )

        every { mockViewModel.stateData } returns MutableStateFlow(mockState)
        every { mockState.error } returns errorState
        every { mockState.hasError } returns true
        every { mockState.isLoading } returns false

        launchFragmentInContainer<PagesFragment>()

        onView(withId(R.id.errorContainer))
            .check(matches(isDisplayed()))

        onView(withId(R.id.errorImageView))
            .check(matches(isDisplayed()))

        onView(withId(R.id.errorTitleTextView))
            .check(matches(isDisplayed()))
            .check(matches(withText("Error Title")))

        onView(withId(R.id.errorMessageTextView))
            .check(matches(isDisplayed()))
            .check(matches(withText("This is an error message")))
    }

    @Test
    fun testLoadingState() {
        every { mockViewModel.stateData } returns MutableStateFlow(mockState)
        every { mockState.isLoading } returns true
        every { mockState.hasError } returns false
        every { mockState.isPageEnabled } returns false
        every { mockState.error } returns null
        every { mockState.hintText } returns null
        every { mockState.bodyText } returns null
        every { mockState.onTextChangeAction } returns null

        launchFragmentInContainer<PagesFragment>()

        onView(withId(R.id.mainEditText))
            .check(matches(isDisplayed()))
            .check(matches(not(isEnabled())))

        onView(withId(R.id.errorContainer))
            .check(matches(not(isDisplayed())))

        onView(withId(R.id.loadingBar))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testCorrectPageState() {
        every { mockViewModel.stateData } returns MutableStateFlow(mockState)

        every { mockState.isLoading } returns false
        every { mockState.hasError } returns false
        every { mockState.isPageEnabled } returns true
        every { mockState.error } returns null
        every { mockState.hintText } returns DynamicString("Hint text")
        every { mockState.bodyText } returns null
        every { mockState.onTextChangeAction } returns null

        launchFragmentInContainer<PagesFragment>()

        onView(withId(R.id.mainEditText))
            .check(matches(isDisplayed()))
            .check(matches(isEnabled()))
            .check(matches(withHint("Hint text")))

        onView(withId(R.id.errorContainer))
            .check(matches(not(isDisplayed())))

        onView(withId(R.id.loadingBar))
            .check(matches(not(isDisplayed())))
    }

    @Test
    fun testCorrectTextValuePageState() {
        every { mockViewModel.stateData } returns MutableStateFlow(mockState)

        every { mockState.isLoading } returns false
        every { mockState.hasError } returns false
        every { mockState.isPageEnabled } returns true
        every { mockState.error } returns null
        every { mockState.hintText } returns DynamicString("Hint text")
        every { mockState.bodyText } returns DynamicString("Value of the text set")
        every { mockState.onTextChangeAction } returns null

        launchFragmentInContainer<PagesFragment>()

        onView(withId(R.id.mainEditText))
            .check(matches(isDisplayed()))
            .check(matches(isEnabled()))
            .check(matches(withText("Value of the text set")))
    }

    @Test
    fun testOnEditTextInputUpdateIsCalled() {
        var testTypedText = ""

        every { mockViewModel.stateData } returns MutableStateFlow(mockState)

        every { mockState.isLoading } returns false
        every { mockState.hasError } returns false
        every { mockState.isPageEnabled } returns true
        every { mockState.error } returns null
        every { mockState.hintText } returns DynamicString("Hint text")
        every { mockState.bodyText } returns DynamicString("")
        every { mockState.onTextChangeAction } returns { testTypedText = it.toString() }

        launchFragmentInContainer<PagesFragment>()

        onView(withId(R.id.mainEditText))
            .check(matches(isDisplayed()))
            .check(matches(isEnabled()))
            .perform(typeText("this is a test"))

        assertEquals("this is a test", testTypedText)
    }

}