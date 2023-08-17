package com.sh.michael.simple_notepad.feature_files.domain

import com.sh.michael.simple_notepad.feature_files.domain.model.IFile
import kotlinx.coroutines.flow.Flow

interface IFilesRepository {

    fun observeAllFiles(): Flow<List<IFile>>

    fun observeAllFilesFiltered(searchQuery: String, pinnedFirst: Boolean = true): Flow<List<IFile>>

    suspend fun createFile(fileName: String, isPinned: Boolean = false)

    suspend fun getFileById(id: String): IFile?

    suspend fun updateFile(id: String, pinnedStatus: Boolean)

    suspend fun deleteFile(id: String)
}