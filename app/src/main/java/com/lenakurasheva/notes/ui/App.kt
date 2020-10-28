package com.lenakurasheva.notes.ui

import android.app.Application
import com.lenakurasheva.notes.di.appModule
import com.lenakurasheva.notes.di.mainModule
import com.lenakurasheva.notes.di.noteModule
import com.lenakurasheva.notes.di.splashModule
import org.koin.android.ext.android.startKoin

class App: Application() {

    companion object {
        var instance: App? = null
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin(this, listOf(appModule, splashModule, mainModule, noteModule))
    }
}