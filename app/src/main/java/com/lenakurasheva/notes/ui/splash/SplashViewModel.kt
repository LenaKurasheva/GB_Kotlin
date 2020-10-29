package com.lenakurasheva.notes.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.lenakurasheva.notes.data.Repository
import com.lenakurasheva.notes.data.entity.User
import com.lenakurasheva.notes.data.errors.NoAuthException
import com.lenakurasheva.notes.ui.base.BaseViewModel


class SplashViewModel(val repository: Repository) : BaseViewModel<Boolean?, SplashViewState>() {

    var currentUserLiveData: LiveData<User?>? = null
    private val currentUserObserver = object : Observer<User?> {
        override fun onChanged(result: User?) {
            viewStateLiveData.value = result?.let { SplashViewState(true) } ?: let {
                SplashViewState(error = NoAuthException())
            }
            currentUserLiveData?.removeObserver(this)
        }
    }

    fun requestUser(){
        currentUserLiveData = repository.getCurrentUser()
        currentUserLiveData?.observeForever(currentUserObserver)
    }

    // Этот метод будет вызван системой при окончательном уничтожении Activity:
    override fun onCleared() {
        // убираем подписку в случае, если посреди запроса пользователь решил выйти назад, например
        currentUserLiveData?.removeObserver(currentUserObserver)
    }
}