package com.sh.michael.simple_notepad.app.data

import android.content.Context
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sh.michael.simple_notepad.feature_files.data.FilesDao
import com.sh.michael.simple_notepad.feature_files.data.model.RoomFile
import com.sh.michael.simple_notepad.feature_notes.data.StickyNotesDao
import com.sh.michael.simple_notepad.feature_notes.data.model.RoomStickyNote
import com.sh.michael.simple_notepad.feature_notes.domain.model.BackgroundColor
import com.sh.michael.simple_notepad.feature_pages.data.PagesDao
import com.sh.michael.simple_notepad.feature_pages.data.model.RoomPage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.UUID

class PrepopulateCallback(context: Context) : RoomDatabase.Callback() {

    private val coroutineScope = CoroutineScope(Job() + Dispatchers.IO)
    private val database: AppDatabase by lazy { AppDatabase.getDatabase(context) }
    private val notesDao: StickyNotesDao by lazy { database.stickyNotesDao() }
    private val filesDao: FilesDao by lazy { database.filesDao() }
    private val pagesDao: PagesDao by lazy { database.pagesDao() }

    private val randomId: String get() = UUID.randomUUID().toString()
    private val randomColor: BackgroundColor get() = BackgroundColor.values()
        .drop(1)
        .toList()
        .random()

    private val defaultFiles = listOf(
        RoomFile(randomId, 0, "Work", isPinned = false),
        RoomFile(randomId, 1, "Home", isPinned = false)
    )

    // todo: possibly show a welcoming message to a user with explanation what
    //  you can do in the app. Additionally you can rename the file to "Explainer"
    private val welcomeText: String? = null

    private val defaultPages = listOf(
        RoomPage(randomId, defaultFiles[0].id, welcomeText),
        RoomPage(randomId, defaultFiles[1].id, null)
    )

    // todo: possibly extract to string resources
    private val defaultNotes = listOf(
        RoomStickyNote(randomId, 0, randomColor, "Hello!"),
        RoomStickyNote(randomId, 1, randomColor, "Swipe up to delete the note")
    )

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        coroutineScope.launch {
            defaultPages.forEach { pagesDao.insert(it) }
            defaultFiles.forEach { filesDao.insert(it) }
            defaultNotes.forEach { notesDao.insert(it) }
        }
    }
}