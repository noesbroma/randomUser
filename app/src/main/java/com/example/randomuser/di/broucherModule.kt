package com.example.randomuser.di

import com.example.randomuser.data.RetrofitBuilder
import com.example.randomuser.data.brochures.BrouchersApiService
import com.example.randomuser.data.brochures.BrouchersRepository
import com.example.randomuser.ui.brouchers.BrouchersViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val broucherModule = module {
    viewModel {
        BrouchersViewModel(get())
    }

    factory { BrouchersRepository(brouchersApiService = get()) }
    factory<BrouchersApiService> { RetrofitBuilder.getRetrofitApi()!!.create(BrouchersApiService::class.java) }
}