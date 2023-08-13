package com.sh.michael.simple_notepad.app.di

import com.sh.michael.simple_notepad.app.data.AppDatabase
import com.sh.michael.simple_notepad.feature_files.di.filesModule
import com.sh.michael.simple_notepad.feature_notes.di.notesModule
import com.sh.michael.simple_notepad.feature_pages.di.pagesModule
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {
    single { AppDatabase.getDatabase(androidApplication()) }
}

val appModules = listOf(appModule, notesModule, pagesModule, filesModule)