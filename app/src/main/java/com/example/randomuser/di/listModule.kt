package com.example.randomuser.di

import com.example.randomuser.data.RetrofitBuilder
import com.example.randomuser.data.list.UserRandomApiService
import com.example.randomuser.data.list.UserRandomRepository
import com.example.randomuser.ui.list.ListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val listModule = module {
    viewModel {
        ListViewModel(get())
    }

    factory { UserRandomRepository(userRandomApiService = get()) }
    factory<UserRandomApiService> { RetrofitBuilder.getRetrofitApi()!!.create(UserRandomApiService::class.java) }
}