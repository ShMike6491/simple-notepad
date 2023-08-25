package com.sh.michael.simple_notepad.feature_pages.data.repository

import com.sh.michael.simple_notepad.app.data.AppDatabase
import com.sh.michael.simple_notepad.feature_pages.data.model.RoomPage
import com.sh.michael.simple_notepad.feature_pages.domain.IPagesRepository
import com.sh.michael.simple_notepad.feature_pages.domain.model.IPage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class PagesRepositoryImpl(
    private val database: AppDatabase
) : IPagesRepository {

    private val pagesDao = database.pagesDao()
    private val filesDao = database.filesDao()

    override fun observePageForId(fileId: String): Flow<IPage?> {
        return pagesDao.observePageOfFile(fileId)
    }

    override suspend fun removePageById(id: String) {
        pagesDao.deleteById(id)
    }

    override suspend fun removePageByFile(fileId: String) {
        pagesDao.deleteByFileId(fileId)
    }

    override suspend fun updatePage(id: String, value: String?) {
        val page = pagesDao.getPageById(id)
        if (page == null || page.pageText == value) return

        pagesDao.update(
            page.copy(pageText = value)
        )
    }

    override suspend fun createPage(fileId: String, value: String?) {
        val newPage = RoomPage(
            id = UUID.randomUUID().toString(),
            fileId = fileId,
            pageText = value
        )

        pagesDao.insert(newPage)
    }

    override suspend fun getPageById(pageId: String): IPage? {
        return pagesDao.getPageById(pageId)
    }

    override suspend fun getPageByFile(fileId: String): IPage? {
        return pagesDao.getPageByFileId(fileId)
    }

    override suspend fun deleteAllFilesData(fileId: String) {
        filesDao.deleteById(fileId)
        // needs to delay due to quick rendering
        delay(400)
        pagesDao.deleteByFileId(fileId)
    }
}