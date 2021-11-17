package com.example.randomuser.di

import com.example.randomuser.ui.detail.DetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val detailModule = module {
    viewModel {
        DetailViewModel()
    }
}