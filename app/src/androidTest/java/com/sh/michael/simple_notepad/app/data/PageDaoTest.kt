package com.sh.michael.simple_notepad.app.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.sh.michael.simple_notepad.feature_pages.data.PagesDao
import com.sh.michael.simple_notepad.feature_pages.data.model.RoomPage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class PageDaoTest {

    lateinit var database: AppDatabase
    private val pagesDao: PagesDao get() = database.pagesDao()

        @Before
    fun setup() {
        val appContext = getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(appContext, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun testInsertAndRetrievePage(): Unit = runBlocking {
        val page = RoomPage(
            "page_id",
            "file_id",
            "This is a test page."
        )

        pagesDao.insert(page)

        val retrievedPage = pagesDao.getPageById("page_id")

        assertEquals("page_id", retrievedPage?.id)
        assertEquals("file_id", retrievedPage?.fileId)
        assertEquals("This is a test page.", retrievedPage?.pageText)
    }

    @Test
    fun testUpdatePage(): Unit = runBlocking {
        val pageId = "page_id"
        val fileId = "file_id"
        val initialPageText = "Initial text"
        val updatedPageText = "Updated text"

        val page = RoomPage(pageId, fileId, initialPageText)
        pagesDao.insert(page)

        val retrievedPage = pagesDao.getPageById(pageId)
        assertEquals(initialPageText, retrievedPage?.pageText)

        retrievedPage?.let {
            val updatedPage = it.copy(pageText = updatedPageText)
            pagesDao.update(updatedPage)

            val retrievedUpdatedPage = pagesDao.getPageById(pageId)
            assertEquals(updatedPageText, retrievedUpdatedPage?.pageText)
        }
    }

    @Test
    fun testDeletePage(): Unit = runBlocking {
        val pageId = "page_id"
        val fileId = "file_id"
        val pageText = "This is a test page."

        val page = RoomPage(pageId, fileId, pageText)
        pagesDao.insert(page)

        val retrievedPage = pagesDao.getPageById(pageId)
        assertEquals(pageId, retrievedPage?.id)

        retrievedPage?.let {
            pagesDao.delete(it)

            val deletedPage = pagesDao.getPageById(pageId)
            assertNull(deletedPage)
        }
    }

    @Test
    fun testObservePage(): Unit = runBlocking {
        val pageId = "page_id"
        val fileId = "file_id"
        val pageText = "This is a test page."

        val page = RoomPage(pageId, fileId, pageText)
        pagesDao.insert(page)

        val observedPage = pagesDao.observePage(pageId).first()
        assertEquals(pageId, observedPage?.id)
        assertEquals(fileId, observedPage?.fileId)
        assertEquals(pageText, observedPage?.pageText)
    }

    @Test
    fun testDeleteByFile(): Unit = runBlocking {
        val fileId = "testId"

        val pageOne = RoomPage("1", fileId, "")
        val pageTwo = RoomPage("2", fileId, "")

        pagesDao.insert(listOf(pageOne, pageTwo))

        assertEquals(2, pagesDao.getAll().first().size)

        pagesDao.deleteByFileId(fileId)

        assertEquals(0, pagesDao.getAll().first().size)
    }

    @Test
    fun testGetPageByFileId(): Unit = runBlocking {
        val fileId = "testId"

        val pageOne = RoomPage("1", "wrong id", "")
        val pageTwo = RoomPage("2", fileId, "")
        val pageThree = RoomPage("3", "other id", "")

        pagesDao.insert(listOf(pageOne, pageTwo, pageThree))

        val testPage = pagesDao.getPageByFileId(fileId)
        assertEquals("2", testPage!!.id)
    }

}