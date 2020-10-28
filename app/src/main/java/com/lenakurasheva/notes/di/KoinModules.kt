package com.lenakurasheva.notes.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.lenakurasheva.notes.data.Repository
import com.lenakurasheva.notes.data.provider.DataProvider
import com.lenakurasheva.notes.data.provider.FirestoreDataProvider
import com.lenakurasheva.notes.ui.main.MainViewModel
import com.lenakurasheva.notes.ui.note.NoteViewModel
import com.lenakurasheva.notes.ui.splash.SplashViewModel

import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module


val appModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FirestoreDataProvider(get(), get()) } bind DataProvider::class
    single { Repository(get()) }
}

val splashModule = module {
    viewModel { SplashViewModel(get()) }
}

val mainModule = module {
    viewModel { MainViewModel(get()) }
}

val noteModule = module {
    viewModel { NoteViewModel(get()) }
}