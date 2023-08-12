package com.sh.michael.simple_notepad.app.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.sh.michael.simple_notepad.feature_files.data.FilesDao
import com.sh.michael.simple_notepad.feature_files.data.model.RoomFile
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class FilesDaoTest {

    lateinit var database: AppDatabase
    private val testDao: FilesDao get() = database.filesDao()

    @Before
    fun setup() {
        val appContext = ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(appContext, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun testInsertAndGetAll() = runBlocking {
        val file = RoomFile(
            id = "1",
            priority = 1,
            title = "test",
            isPinned = false
        )
        testDao.insert(file)

        val files = testDao.getAll().firstOrNull()
        Assert.assertEquals(1, files?.size)
        Assert.assertEquals(file, files?.get(0))
    }

    @Test
    fun testUpdateAndGetFileById() = runBlocking {
        val file = RoomFile(
            id = "1",
            priority = 1,
            title = "test",
            isPinned = false
        )
        testDao.insert(file)

        val updatedFile = file.copy(title = "Updated note")
        testDao.update(updatedFile)

        val retrievedFile = testDao.getFileById("1")
        Assert.assertEquals(updatedFile, retrievedFile)
    }

    @Test
    fun testDeleteAndGetAllByPriorityDesc() = runBlocking {
        val file1 = RoomFile(
            id = "1",
            priority = 1,
            title = "test 1",
            isPinned = false
        )
        val file2 = RoomFile(
            id = "2",
            priority = 2,
            title = "test 2",
            isPinned = false
        )
        testDao.insert(file1, file2)

        testDao.delete(file1)

        val files = testDao.getAllByPriority().firstOrNull()
        Assert.assertEquals(1, files?.size)
        Assert.assertEquals(file2, files?.get(0))
    }


    @Test
    fun testInsertWithOnConflictIgnore() = runBlocking {
        val file1 = RoomFile(
            id = "1",
            priority = 1,
            title = "test 1",
            isPinned = false
        )
        val file2 = RoomFile(
            id = "1",
            priority = 2,
            title = "test 2",
            isPinned = false
        )

        testDao.insert(file1)
        testDao.insert(file2)

        val files = testDao.getAll().firstOrNull()
        Assert.assertEquals(1, files?.size)
        Assert.assertEquals(file1, files?.get(0))
    }

    @Test
    fun testGetHighestPriority() = runBlocking {
        val file1 = RoomFile(
            id = "1",
            priority = 1,
            title = "test 1",
            isPinned = false
        )
        val file2 = RoomFile(
            id = "2",
            priority = 2,
            title = "test 2",
            isPinned = false
        )
        testDao.insert(file1, file2)

        val highestPriority = testDao.getHighestPriority()
        Assert.assertEquals(2, highestPriority)
    }
}