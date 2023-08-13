package com.sh.michael.simple_notepad.feature_files.data.repository

import com.sh.michael.simple_notepad.app.data.AppDatabase
import com.sh.michael.simple_notepad.feature_files.data.FilesDao
import com.sh.michael.simple_notepad.feature_files.data.model.RoomFile
import com.sh.michael.simple_notepad.feature_files.domain.IFilesRepository
import com.sh.michael.simple_notepad.feature_files.domain.model.IFile
import com.sh.michael.simple_notepad.feature_pages.data.PagesDao
import com.sh.michael.simple_notepad.feature_pages.data.model.RoomPage
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class FilesRepositoryImpl(
    private val database: AppDatabase
) : IFilesRepository {

    private val filesDao: FilesDao = database.filesDao()
    private val pagesDao: PagesDao = database.pagesDao()

    override fun observeAllFiles(): Flow<List<IFile>> = filesDao.getAllByPriority()

    override fun observeAllFilesFiltered(searchQuery: String, pinnedFirst: Boolean): Flow<List<IFile>> {
        // todo: fix later when search feature is enabled
        return filesDao.getAllByPriority()
    }

    override suspend fun createFile(fileName: String, isPinned: Boolean) {
        // fixme: eventually priority will get to MAX_INT_VALUE and we will need to deal with it
        // fixme: move creation of a page to an interactor??
        val highestPriority = filesDao.getHighestPriority() ?: 0
        val fileId = UUID.randomUUID().toString()
        val newFile = RoomFile(
            id = fileId,
            priority = highestPriority + 1,
            title = fileName,
            isPinned = isPinned
        )

        val newPage = RoomPage(
            id = UUID.randomUUID().toString(),
            fileId = fileId,
            pageText = null
        )

        pagesDao.insert(newPage)
        filesDao.insert(newFile)
    }

    override suspend fun getFileById(id: String): IFile? = filesDao.getFileById(id)

    override suspend fun updateFile(id: String, pinnedStatus: Boolean) {
        val file = filesDao.getFileById(id)
        if (file == null || file.isPinned == pinnedStatus) return

        filesDao.update(
            file.copy(isPinned = pinnedStatus)
        )
    }

    override suspend fun deleteFile(id: String) = filesDao.deleteById(id)
}