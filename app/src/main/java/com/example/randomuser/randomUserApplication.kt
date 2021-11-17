package com.example.randomuser.ui

import android.app.Application
import com.example.randomuser.di.detailModule
import com.example.randomuser.di.listModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class randomUserApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startDependencyInjection()
    }


    private fun startDependencyInjection() {
        startKoin {
            androidContext(this@randomUserApplication)
            modules(
                listOf(
                    listModule,
                    detailModule
                )
            )
        }
    }
}