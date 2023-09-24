package com.sh.michael.simple_notepad.feature_files.ui

import com.sh.michael.simple_notepad.feature_files.domain.IFilesRepository
import com.sh.michael.simple_notepad.feature_files.domain.model.IFile
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class FilesViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private val mockRepo: IFilesRepository = mockk()
    private lateinit var viewModel: FilesViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
        clearMocks(mockRepo)
    }

    @Test
    fun `test empty files list is passed`() {
        every { mockRepo.observeAllFiles() } returns flowOf(emptyList())

        viewModel = FilesViewModel(mockRepo)

        assertEquals(true, viewModel.stateData.value.isEmpty())
    }

    @Test
    fun `test observed file is mapped correctly`() {
        val testFile = object : IFile {
            override val priority: Int = 1
            override val title: String = "Test title"
            override val isPinned: Boolean = true
            override val id: String = "test id"
        }

        every { mockRepo.observeAllFiles() } returns flowOf(listOf(testFile))

        viewModel = FilesViewModel(mockRepo)

        val actualState = viewModel.stateData.value.firstOrNull()

        assertEquals("test id", actualState?.id)
    }
}