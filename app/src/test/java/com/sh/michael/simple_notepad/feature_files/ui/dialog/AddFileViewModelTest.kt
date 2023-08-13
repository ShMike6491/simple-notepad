package com.sh.michael.simple_notepad.feature_files.ui.dialog

import com.sh.michael.simple_notepad.common.model.UiEvent
import com.sh.michael.simple_notepad.common.model.testString
import com.sh.michael.simple_notepad.feature_files.domain.IFilesRepository
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
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

class AddFileViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private val mockRepo: IFilesRepository = mockk()
    private lateinit var viewModel: AddFileViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = AddFileViewModel(mockRepo)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
        clearMocks(mockRepo)
    }

    @Test
    fun `test initial state data`() {
        val expectedFooter = "0/50"

        val actualState = viewModel.pageState.value

        assertEquals(expectedFooter, actualState.footerText?.testString)
        assertEquals(true, actualState.helperText != null)
        assertEquals(true, actualState.onCloseAction != null)
        assertEquals(true, actualState.onSubmitAction != null)
        assertEquals(true, actualState.titleChangeListener != null)
    }

    @Test
    fun `test title change listener updates title value`() {
        val testTitle = "this is a test"

        viewModel.pageState
            .value
            .titleChangeListener
            ?.invoke(testTitle)

        val expected = viewModel.pageState.value

        assertEquals("this is a test", expected.value)
    }

    @Test
    fun `test title change listener updates footer counter value`() {
        val testTitle = "test"

        viewModel.pageState
            .value
            .titleChangeListener
            ?.invoke(testTitle)

        val expected = viewModel.pageState.value

        assertEquals("4/50", expected.footerText?.testString)
    }

    @Test
    fun `test on submit will trigger repository update if successful`() = runBlocking {
        val titleSlot = slot<String>()
        coEvery { mockRepo.createFile(capture(titleSlot), any()) } returns Unit

        viewModel.pageState
            .value
            .titleChangeListener
            ?.invoke("test title")

        viewModel.pageState.value.onSubmitAction?.invoke()

        coVerify(exactly = 1) { mockRepo.createFile(any(), any()) }
        assertEquals("test title", titleSlot.captured)
    }

    @Test
    fun `test on close action will send close event`() = runBlocking {
        viewModel.pageState.value.onCloseAction?.invoke()

        val expected = UiEvent.PopBackStack
        val actual = viewModel.uiEvent.first()

        assertEquals(expected, actual)
    }

    @Test
    fun `test on submit action will send error snackbar event if no title set`() = runBlocking {
        viewModel.pageState.value.onSubmitAction?.invoke()

        val actual = viewModel.uiEvent.first()

        assertEquals(true, actual is UiEvent.ShowSnackbar)
    }

    @Test
    fun `test on submit action will send error snackbar event if a long title is set`() = runBlocking {
        val longTitle = (1..100).joinToString { it.toString() }

        coEvery { mockRepo.createFile(any(), any()) } returns Unit

        viewModel.pageState.value.apply {
            titleChangeListener?.invoke(longTitle)
            onSubmitAction?.invoke()
        }

        val actual = viewModel.uiEvent.first()

        assertEquals(true, actual is UiEvent.ShowSnackbar)
    }

    @Test
    fun `test on submit action will send secondary snackbar successful`() = runBlocking {
        val longTitle = "some title"

        coEvery { mockRepo.createFile(any(), any()) } returns Unit

        viewModel.pageState.value.apply {
            titleChangeListener?.invoke(longTitle)
            onSubmitAction?.invoke()
        }

        val actual = viewModel.uiEvent.first()
        val closeActual = viewModel.uiEvent.first()

        assertEquals(true, actual is UiEvent.ShowSecondarySnackbar)
        assertEquals(UiEvent.PopBackStack, closeActual)
    }
}