package com.sh.michael.simple_notepad.feature_menu.di

import com.sh.michael.simple_notepad.feature_menu.ui.SideMenuViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val menuModule = module {
    viewModel { SideMenuViewModel() }
}