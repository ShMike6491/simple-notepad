package com.sh.michael.simple_notepad.feature_pages.data.repository

import com.sh.michael.simple_notepad.app.data.AppDatabase
import com.sh.michael.simple_notepad.feature_pages.domain.IPagesRepository
import com.sh.michael.simple_notepad.feature_pages.domain.model.IPage
import kotlinx.coroutines.flow.Flow

class PagesRepositoryImpl(
    private val database: AppDatabase
) : IPagesRepository {

    private val pagesDao = database.pagesDao()

    override fun observePageForId(fileId: String): Flow<IPage> {
        TODO("Not yet implemented")
    }

    override suspend fun removePageById(id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun removePageByFile(fileId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun updatePage(id: String, value: String?) {
        TODO("Not yet implemented")
    }

    override suspend fun createPage(fileId: String, value: String?) {
        TODO("Not yet implemented")
    }
}