package com.sh.michael.simple_notepad.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sh.michael.simple_notepad.feature_files.data.FilesDao
import com.sh.michael.simple_notepad.feature_files.data.model.RoomFile
import com.sh.michael.simple_notepad.feature_notes.data.BackgroundConverter
import com.sh.michael.simple_notepad.feature_notes.data.StickyNotesDao
import com.sh.michael.simple_notepad.feature_notes.data.model.RoomStickyNote
import com.sh.michael.simple_notepad.feature_pages.data.PagesDao
import com.sh.michael.simple_notepad.feature_pages.data.model.RoomPage

@Database(
    entities = [
        RoomStickyNote::class,
        RoomPage::class,
        RoomFile::class,
    ],
    version = 4
)
@TypeConverters(BackgroundConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun stickyNotesDao(): StickyNotesDao
    abstract fun pagesDao(): PagesDao
    abstract fun filesDao(): FilesDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null
        private const val DATABASE_TAG = "app_database"

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    DATABASE_TAG
                )
                    // todo: convert to migration strategies when app is in production
                    //  https://developer.android.com/training/data-storage/room/migrating-db-versions.html
                    .fallbackToDestructiveMigration()
                    .build()
                    .initDefaultValues()

                INSTANCE = instance
                instance
            }
        }
    }
}

fun AppDatabase.initDefaultValues(): AppDatabase {
    // todo: initialize database with default files and pages data
    //  possibly use other approach
    return this.apply {

    }
}