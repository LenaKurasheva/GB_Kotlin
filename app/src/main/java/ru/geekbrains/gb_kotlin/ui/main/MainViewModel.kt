package ru.geekbrains.gb_kotlin.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.geekbrains.gb_kotlin.data.Repository

class MainViewModel : ViewModel() {

    private val viewStateLiveData: MutableLiveData<MainViewState> = MutableLiveData()

    init {
        viewStateLiveData.value = MainViewState(Repository.notes)
    }

    // Не обращаемся к свойству viewStateLiveData напрямую, чтобы его не могли изменить извне:
    fun viewState(): LiveData<MainViewState> = viewStateLiveData
}