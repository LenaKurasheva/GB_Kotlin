package com.lenakurasheva.notes.ui.splash

import com.lenakurasheva.notes.data.Repository
import com.lenakurasheva.notes.data.errors.NoAuthException
import com.lenakurasheva.notes.ui.base.BaseViewModel
import kotlinx.coroutines.launch


class SplashViewModel(val repository: Repository) : BaseViewModel<Boolean>() {

    fun requestUser() = launch {
        repository.getCurrentUser()?.let {
            setData(true)
        } ?: setError(NoAuthException())
    }
}
