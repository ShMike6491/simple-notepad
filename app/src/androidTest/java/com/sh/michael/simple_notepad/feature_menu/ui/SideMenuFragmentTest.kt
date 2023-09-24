package com.sh.michael.simple_notepad.feature_menu.ui

import android.content.Intent
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.sh.michael.simple_notepad.R
import com.sh.michael.simple_notepad.common.model.UiEvent
import com.sh.michael.simple_notepad.common.model.UiString.*
import com.sh.michael.simple_notepad.feature_menu.ui.model.MenuItemState
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.allOf
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
class SideMenuFragmentTest {

    private val mockViewModel: SideMenuViewModel = mockk(relaxed = true)
    private val mockEvent: UiEvent = mockk()

    @Before
    fun setup() {
        Intents.init()

        val module = module {
            viewModel { mockViewModel }
        }

        startKoin {
            modules(module)
        }
    }

    @After
    fun teardown() {
        Intents.release()

        stopKoin()
        clearMocks(
            mockViewModel,
            mockEvent
        )
    }

    @Test
    fun testContactMenuIsShownCorrectly() {
        val mockState = MenuItemState(
            "id",
            DynamicString("Test title"),
            R.drawable.ic_plus
        )

        every { mockViewModel.menuState } returns listOf(mockState)

        launchFragmentInContainer<SideMenuFragment>()

        onView(withId(R.id.titleTextView))
            .check(matches(isDisplayed()))
            .check(matches(withText("Test title")))

        onView(withId(R.id.iconImageView))
            .check(matches(isDisplayed()))

        onView(withId(R.id.trailingImageView))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testExitIsShown() {
        val mockState = MenuItemState("")
        every { mockViewModel.menuState } returns listOf(mockState)

        launchFragmentInContainer<SideMenuFragment>()

        onView(withId(R.id.exitContainer))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testExitClickTriggersViewModelExit() {
        val mockState = MenuItemState("")
        every { mockViewModel.menuState } returns listOf(mockState)
        every { mockViewModel.onExitClick() } returns Unit

        launchFragmentInContainer<SideMenuFragment>()

        onView(withId(R.id.exitContainer))
            .perform(click())

        verify(exactly = 1) { mockViewModel.onExitClick() }
    }

    @Test
    fun testContactMenuClickTriggersStateMethod() {
        var hasBeenCalled = false
        val mockState = MenuItemState("") { hasBeenCalled = true }
        every { mockViewModel.menuState } returns listOf(mockState)

        launchFragmentInContainer<SideMenuFragment>()

        onView(withId(R.id.trailingImageView))
            .perform(click())

        assertEquals(true, hasBeenCalled)
    }

    @Test
    fun testNavigationToMailWorksAsExpected(): Unit = runBlocking {
        val testChannel = Channel<UiEvent>()
        val mockState = MenuItemState("")
        every { mockViewModel.menuState } returns listOf(mockState)
        every { mockViewModel.uiEvent } returns testChannel.receiveAsFlow()

        launchFragmentInContainer<SideMenuFragment>()

        testChannel.send(UiEvent.Navigate("action_menu_navigate_to_contacts"))

        val expectedIntent = allOf(hasAction(Intent.ACTION_SENDTO))
        intended(expectedIntent)
    }

    @Test
    fun testNavigationToPrivacyWebWorksAsExpected(): Unit = runBlocking {
        val testUrl = "https://www.google.com"
        val testChannel = Channel<UiEvent>()
        val mockState = MenuItemState("")
        every { mockViewModel.menuState } returns listOf(mockState)
        every { mockViewModel.uiEvent } returns testChannel.receiveAsFlow()

        launchFragmentInContainer<SideMenuFragment>()

        testChannel.send(UiEvent.Web(testUrl))

        val expectedIntent = allOf(hasAction(Intent.ACTION_VIEW), IntentMatchers.hasData(testUrl))
        intended(expectedIntent)
    }
}