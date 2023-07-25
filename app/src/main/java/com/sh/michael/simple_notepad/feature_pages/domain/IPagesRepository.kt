package com.sh.michael.simple_notepad.feature_pages.domain

import com.sh.michael.simple_notepad.feature_pages.domain.model.IPage
import kotlinx.coroutines.flow.Flow

interface IPagesRepository {

    fun observePageForId(fileId: String): Flow<IPage>

    suspend fun removePageById(id: String)

    suspend fun removePageByFile(fileId: String)

    suspend fun updatePage(id: String, value: String?)

    suspend fun createPage(fileId: String, value: String?)
}