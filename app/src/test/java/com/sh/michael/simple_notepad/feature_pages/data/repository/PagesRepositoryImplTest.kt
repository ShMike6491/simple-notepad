package com.sh.michael.simple_notepad.feature_pages.data.repository

import com.sh.michael.simple_notepad.app.data.AppDatabase
import com.sh.michael.simple_notepad.feature_pages.data.PagesDao
import com.sh.michael.simple_notepad.feature_pages.data.model.RoomPage
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class PagesRepositoryImplTest {

    private val mockDatabase: AppDatabase = mockk()
    private val mockDao: PagesDao = mockk(relaxed = true)

    private lateinit var repository: PagesRepositoryImpl

    // todo: add tests for delete action

    @Before
    fun setup() {
        every { mockDatabase.pagesDao() } returns mockDao
        repository = PagesRepositoryImpl(mockDatabase)
    }

    @After
    fun teardown() {
        clearMocks(mockDao, mockDatabase)
    }


    @Test
    fun `test observePageForId`() = runBlocking {
        val fileId = "file_id"
        val mockPage: RoomPage = mockk()
        coEvery { mockDao.observePageOfFile(fileId) } returns flowOf(mockPage)

        val result = repository.observePageForId(fileId).first()

        assertEquals(mockPage, result)
        coVerify { mockDao.observePageOfFile(fileId) }
    }

    @Test
    fun `test removePageById`() = runBlocking {
        val id = "page_id"
        coEvery { mockDao.deleteById(id) } just runs

        repository.removePageById(id)

        coVerify { mockDao.deleteById(id) }
    }

    @Test
    fun `test removePageByFile`() = runBlocking {
        val fileId = "file_id"
        coEvery { mockDao.deleteByFileId(fileId) } just runs

        repository.removePageByFile(fileId)

        coVerify { mockDao.deleteByFileId(fileId) }
    }

    @Test
    fun `test updatePage`() = runBlocking {
        val id = "page_id"
        val value = "updated_value"
        val mockPage = RoomPage(
            id = id,
            fileId = "fileId",
            pageText = null
        )

        coEvery { mockDao.getPageById(id) } returns mockPage
        coEvery { mockDao.update(any<RoomPage>()) } just runs

        repository.updatePage(id, value)

        coVerify {
            mockDao.getPageById(id)
            mockDao.update(withArg<RoomPage> {
                assert(it.pageText == value)
            })
        }
    }

    @Test
    fun `test updatePage with the same value will not be updated`() = runBlocking {
        val id = "page_id"
        val value = "updated_value"
        val mockPage = RoomPage(
            id = id,
            fileId = "fileId",
            pageText = "updated_value"
        )

        coEvery { mockDao.getPageById(id) } returns mockPage
        coEvery { mockDao.update(any<RoomPage>()) } just runs

        repository.updatePage(id, value)

        coVerify(exactly = 1) { mockDao.getPageById(id) }
        coVerify(exactly = 0) { mockDao.update(any<RoomPage>()) }
    }

    @Test
    fun `test createPage`() = runBlocking {
        val fileId = "file_id"
        val value = "new_page_value"
        val mockNewPage: RoomPage = mockk()
        every { mockNewPage.id } returns "new_page_id"
        every { mockNewPage.fileId } returns fileId
        coEvery { mockDao.insert(any<RoomPage>()) } just runs

        repository.createPage(fileId, value)

        coVerify {
            mockDao.insert(withArg<RoomPage> {
                assert(it.fileId == fileId)
                assert(it.pageText == value)
            })
        }
    }

    @Test
    fun `test getPageById`() = runBlocking {
        val pageId = "page_id"
        val mockPage: RoomPage = mockk()
        coEvery { mockDao.getPageById(pageId) } returns mockPage

        val result = repository.getPageById(pageId)

        assert(result == mockPage)
        coVerify { mockDao.getPageById(pageId) }
    }

    @Test
    fun `test getPageByFile`() = runBlocking {
        val fileId = "file_id"
        val mockPage: RoomPage = mockk()
        coEvery { mockDao.getPageByFileId(fileId) } returns mockPage

        val result = repository.getPageByFile(fileId)

        assert(result == mockPage)
        coVerify { mockDao.getPageByFileId(fileId) }
    }
}