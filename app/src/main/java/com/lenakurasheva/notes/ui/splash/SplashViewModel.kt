package com.lenakurasheva.notes.ui.splash

import com.lenakurasheva.notes.data.Repository
import com.lenakurasheva.notes.data.errors.NoAuthException
import com.lenakurasheva.notes.ui.base.BaseViewModel

class SplashViewModel : BaseViewModel<Boolean?, SplashViewState>() {
    fun requestUser(){
        Repository.getCurrentUser().observeForever{    //TODO remove observer
            viewStateLiveData.value = it?.let { SplashViewState(true) } ?: let {
                SplashViewState(error = NoAuthException())
            }
        }
    }
}