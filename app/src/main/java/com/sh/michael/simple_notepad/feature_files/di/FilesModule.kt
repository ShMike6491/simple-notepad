package com.sh.michael.simple_notepad.feature_files.di

import com.sh.michael.simple_notepad.feature_files.data.repository.FilesRepositoryImpl
import com.sh.michael.simple_notepad.feature_files.domain.IFilesRepository
import com.sh.michael.simple_notepad.feature_files.ui.FilesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val filesModule = module {

    single { FilesRepositoryImpl(database = get()) as IFilesRepository }

    viewModel { FilesViewModel(repository = get()) }
}