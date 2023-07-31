package com.sh.michael.simple_notepad.feature_pages.di

import com.sh.michael.simple_notepad.feature_pages.data.repository.PagesRepositoryImpl
import com.sh.michael.simple_notepad.feature_pages.domain.IPagesRepository
import com.sh.michael.simple_notepad.feature_pages.ui.PagesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val pagesModule = module {
    single { PagesRepositoryImpl(database = get()) as IPagesRepository }

    viewModel { (id: String?) ->
        PagesViewModel(fileId = id, repository = get())
    }
}