package com.example.randomuser.ui

import android.app.Application
import android.content.Context
import com.example.randomuser.di.detailModule
import com.example.randomuser.di.listModule
import okhttp3.internal.Internal
import okhttp3.internal.Internal.instance
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


    companion object {
        val app: Internal?
            get() = instance
    }
}