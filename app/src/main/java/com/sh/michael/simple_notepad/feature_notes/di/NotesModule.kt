package com.sh.michael.simple_notepad.feature_notes.di

import com.sh.michael.simple_notepad.feature_notes.data.repository.StickyNoteRepositoryImpl
import com.sh.michael.simple_notepad.feature_notes.domain.IStickyNoteRepository
import com.sh.michael.simple_notepad.feature_notes.ui.StickyNotesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val notesModule = module {
    single { StickyNoteRepositoryImpl(database = get()) as IStickyNoteRepository }

    viewModel { StickyNotesViewModel(repo = get()) }
}