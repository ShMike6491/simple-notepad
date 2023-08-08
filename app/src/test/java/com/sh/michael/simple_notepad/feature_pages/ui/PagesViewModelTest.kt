package com.sh.michael.simple_notepad.feature_pages.ui

import com.sh.michael.simple_notepad.common.model.UiEvent
import com.sh.michael.simple_notepad.feature_pages.domain.IPagesRepository
import com.sh.michael.simple_notepad.feature_pages.domain.model.IPage
import com.sh.michael.simple_notepad.feature_pages.ui.model.PageState
import io.mockk.Called
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class PagesViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private val mockRepo: IPagesRepository = mockk()
    private lateinit var viewModel: PagesViewModel
    private val testObserverState = mutableListOf<PageState>()

    // todo: add tests for delete action

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
        testObserverState.clear()
        clearMocks(mockRepo)
    }

    @Test
    fun `init without fileId should show error state and error message`() = runBlockingTest {
        val argumentSlot = slot<String>()
        coEvery { mockRepo.observePageForId(capture(argumentSlot)) } returns flowOf(null)

        viewModel = PagesViewModel(null, mockRepo)

        // When
        val testState = viewModel.stateData.value

        // Then
        assertEquals(true, testState.error != null)
        assertEquals(true, testState.isLoading.not())
        assertEquals("", argumentSlot.captured)
        verify(exactly = 1) { mockRepo.observePageForId(any()) }
    }

    @Test
    fun `init with fileId but page not found should show error state and error message`() = runBlockingTest {
        val argumentSlot = slot<String>()
        coEvery { mockRepo.observePageForId(capture(argumentSlot)) } returns flowOf(null)

        viewModel = PagesViewModel("fileId", mockRepo)

        // When
        val testState = viewModel.stateData.value

        // Then
        assertEquals(true, testState.error != null)
        assertEquals(true, testState.isLoading.not())
        assertEquals("fileId", argumentSlot.captured)
        verify(exactly = 1) { mockRepo.observePageForId("fileId") }
    }

    @Test
    fun `init with fileId and page found should push page update`() = runBlockingTest {
        val argumentSlot = slot<String>()
        val testPage = object : IPage {
            override val fileId: String = "fileId"
            override val pageText: String = "this is the page text"
            override val id: String = "pageId"
        }

        every { mockRepo.observePageForId(capture(argumentSlot)) } returns flowOf(testPage)

        viewModel = PagesViewModel("fileId", mockRepo)

        // When
        val testState = viewModel.stateData.value

        // Then
        assertEquals("this is the page text", testState.valueText)
        assertEquals(true, testState.isLoading.not())
        assertEquals(true, testState.isPageEnabled)
        assertEquals(true, testState.onTextChangeAction != null)
        assertEquals("fileId", argumentSlot.captured)
        verify(exactly = 1) { mockRepo.observePageForId("fileId") }
    }

    @Test
    fun `onTextChanged should update state and call repository's updatePage`() = runBlockingTest {
        val idSlot = slot<String>()
        val textSlot = slot<String>()

        val testPage = object : IPage {
            override val fileId: String = "fileId"
            override val pageText: String = "this is the page text"
            override val id: String = "pageId"
        }

        every { mockRepo.observePageForId(any()) } returns flowOf(testPage)
        coEvery { mockRepo.updatePage(capture(idSlot), capture(textSlot)) } returns Unit

        viewModel = PagesViewModel("fileId", mockRepo)

        // When
        val testState = viewModel.stateData.value
        testState.onTextChangeAction?.invoke("testing new text being passed")

        // Then
        assertEquals("pageId", idSlot.captured)
        assertEquals("testing new text being passed", textSlot.captured)
        verify(exactly = 1) { mockRepo.observePageForId("fileId") }
        coVerify(exactly = 1) { mockRepo.updatePage(any(), any()) }
    }

    @Test
    fun `onTextChanged should not update state when text is the same`() = runBlockingTest {
        val testPage = object : IPage {
            override val fileId: String = "fileId"
            override val pageText: String = "this is the page text"
            override val id: String = "pageId"
        }

        every { mockRepo.observePageForId(any()) } returns flowOf(testPage)
        coEvery { mockRepo.updatePage(any(), any()) } returns Unit

        viewModel = PagesViewModel("fileId", mockRepo)

        // When
        val testState = viewModel.stateData.value
        testState.onTextChangeAction?.invoke("this is the page text")

        // Then
        verify(exactly = 1) { mockRepo.observePageForId("fileId") }
        coVerify(exactly = 0) { mockRepo.updatePage(any(), any()) }
    }

    @Test
    fun `showErrorMessage should send an event with the error message`() = runBlockingTest {
        every { mockRepo.observePageForId(any()) } returns flowOf(null)

        val viewModel = PagesViewModel(null, mockRepo)

        // When
        val testEvent = viewModel.uiEvent.first()
        viewModel.stateData.value

        // Then
        assertEquals(true, testEvent is UiEvent.ShowSnackbar)
    }
}