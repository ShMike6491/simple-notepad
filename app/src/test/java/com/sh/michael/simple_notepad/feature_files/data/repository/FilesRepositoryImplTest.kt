package com.sh.michael.simple_notepad.feature_files.data.repository

import com.sh.michael.simple_notepad.app.data.AppDatabase
import com.sh.michael.simple_notepad.feature_files.data.FilesDao
import com.sh.michael.simple_notepad.feature_files.data.model.RoomFile
import com.sh.michael.simple_notepad.feature_pages.data.PagesDao
import com.sh.michael.simple_notepad.feature_pages.data.model.RoomPage
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.UUID

class FilesRepositoryImplTest {

    private val mockDatabase: AppDatabase = mockk()
    private val mockFilesDao: FilesDao = mockk(relaxed = true)
    private val mockPagesDao: PagesDao = mockk(relaxed = true)

    private lateinit var repository: FilesRepositoryImpl

    // todo: add tests for delete action

    @Before
    fun setup() {
        every { mockDatabase.filesDao() } returns mockFilesDao
        every { mockDatabase.pagesDao() } returns mockPagesDao
        repository = FilesRepositoryImpl(mockDatabase)
    }

    @After
    fun teardown() {
        clearMocks(mockFilesDao, mockDatabase, mockPagesDao)
    }

    @Test
    fun `observeAllFiles returns flow of files`() = runBlockingTest {
        val mockNote1: RoomFile = mockk()
        val mockNote2: RoomFile = mockk()
        val mockFilesList = listOf(mockNote1, mockNote2)
        every { mockFilesDao.getAllByPriority() } returns flowOf(mockFilesList)

        val flowResult = repository.observeAllFiles().first()

        assertEquals(mockFilesList, flowResult)
        coVerify { mockFilesDao.getAllByPriority() }
    }

    @Test
    fun `removeFileById calls deleteById on filesDao`() = runBlockingTest {
        val noteId = UUID.randomUUID().toString()

        repository.deleteFile(noteId)

        coVerify { mockFilesDao.deleteById(noteId) }
    }

    @Test
    fun `createFile inserts a new file and a new page with the file id`() = runBlockingTest {
        val fileSlot = slot<RoomFile>()
        val fileText = "Sample file"
        val isPinned = true

        coEvery { mockFilesDao.getHighestPriority() } returns 5
        coEvery { mockFilesDao.insert(capture(fileSlot)) } just runs
        coEvery { mockPagesDao.insert(any<RoomPage>()) } just runs

        repository.createFile(fileText, isPinned)

        coVerify {
            mockFilesDao.insert(
                match<RoomFile> { file ->
                    file.title == fileText &&
                        file.isPinned == isPinned
                }
            )
        }

        coVerify {
            mockPagesDao.insert(
                match<RoomPage> { page ->
                    page.fileId == fileSlot.captured.id &&
                        page.pageText == null
                }
            )
        }
    }

    @Test
    fun `createFile inserts a new file with priority 1 when first file created`() = runBlockingTest {
        val fileTitle = "Sample note"
        val mockFile = RoomFile(
            id = UUID.randomUUID().toString(),
            priority = -1,
            title = fileTitle,
            isPinned = false
        )

        coEvery { mockFilesDao.getHighestPriority() } returns null

        repository.createFile(fileTitle)

        // Verify that the insert function was called on notesDao with the correct parameters
        coVerify {
            mockFilesDao.insert(
                match<RoomFile> { note ->
                    note.priority == 1 && note.title == mockFile.title
                }
            )
        }
    }
}