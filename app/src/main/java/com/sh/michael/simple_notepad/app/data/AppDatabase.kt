package com.sh.michael.simple_notepad.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sh.michael.simple_notepad.feature_notes.data.BackgroundConverter
import com.sh.michael.simple_notepad.feature_notes.data.StickyNotesDao
import com.sh.michael.simple_notepad.feature_notes.data.model.RoomStickyNote
import com.sh.michael.simple_notepad.feature_pages.data.PagesDao
import com.sh.michael.simple_notepad.feature_pages.data.model.RoomPage

@Database(
    entities = [
        RoomStickyNote::class,
        RoomPage::class
    ],
    version = 3
)
@TypeConverters(BackgroundConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun stickyNotesDao(): StickyNotesDao
    abstract fun pagesDao(): PagesDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null
        private const val DATABASE_TAG = "app_database"

        // todo: initialize database with default files and pages data
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

                INSTANCE = instance
                instance
            }
        }
    }
}