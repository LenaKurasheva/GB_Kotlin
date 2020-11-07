package com.lenakurasheva.notes.ui.main

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.Observer
import com.lenakurasheva.notes.data.Repository
import com.lenakurasheva.notes.data.entity.Note
import com.lenakurasheva.notes.data.model.NoteResult
import com.lenakurasheva.notes.ui.base.BaseViewModel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

class MainViewModel(val repository: Repository) : BaseViewModel<List<Note>?>() {

    private val repositoryNotes = repository.getNotes()

    init {
        launch {
            repositoryNotes.consumeEach { result ->
                when (result){
                    is NoteResult.Success<*> -> setData(result.data as? List<Note>)
                    is NoteResult.Error -> setError(result.error)
                }
            }
        }
    }

    @VisibleForTesting
    public override fun onCleared() {
        super.onCleared()
        repositoryNotes.cancel()
    }
}