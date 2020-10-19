package com.LenaKurasheva.notes.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.LenaKurasheva.notes.data.Repository
import com.LenaKurasheva.notes.data.model.Note
import com.LenaKurasheva.notes.data.model.NoteResult
import com.LenaKurasheva.notes.ui.base.BaseViewModel

class MainViewModel : BaseViewModel<List<Note>?, MainViewState>() {

    private val notesObserver = Observer{ result: NoteResult? ->
        result ?: return@Observer
        when (result){
            is NoteResult.Success<*> -> viewStateLiveData.value = MainViewState(result.data as? List<Note>)
            is NoteResult.Error -> viewStateLiveData.value = MainViewState(error = result.error)
        }
    }

    private val repositoryNotes = Repository.getNotes()

    init {
        repositoryNotes.observeForever(notesObserver)
    }

    fun viewState(): LiveData<MainViewState> = viewStateLiveData

    override fun onCleared() {
        super.onCleared()
        repositoryNotes.removeObserver(notesObserver)
    }
}