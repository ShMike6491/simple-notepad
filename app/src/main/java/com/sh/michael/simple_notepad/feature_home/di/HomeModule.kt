package com.sh.michael.simple_notepad.feature_home.di

import com.sh.michael.simple_notepad.feature_home.ui.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {

    viewModel { HomeViewModel() }
}