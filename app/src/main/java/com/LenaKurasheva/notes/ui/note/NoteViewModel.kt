package com.LenaKurasheva.notes.ui.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.LenaKurasheva.notes.data.Repository
import com.LenaKurasheva.notes.data.model.Note
import com.LenaKurasheva.notes.data.model.NoteResult
import com.LenaKurasheva.notes.ui.base.BaseViewModel

class NoteViewModel : BaseViewModel<Note?, NoteViewState>() {

    init {
        viewStateLiveData.value = NoteViewState()
    }

    private var pendingNote: Note? = null

    private val notesObserver = Observer{ result: NoteResult? ->
        result ?: return@Observer
        when (result){
            is NoteResult.Success<*> -> viewStateLiveData.value = NoteViewState(result.data as? Note)
            is NoteResult.Error -> viewStateLiveData.value = NoteViewState(error = result.error)
        }
    }

    var NoteByIdLiveData: LiveData<NoteResult>? = null

    fun saveChanges(note: Note) {
        pendingNote = note
    }

    // Этот метод будет вызван системой при окончательном уничтожении Activity:
    override fun onCleared() {
        pendingNote?.let {
            Repository.saveNote(it)
        }
        NoteByIdLiveData?.removeObserver(notesObserver)
    }

    fun loadNote(id: String){
        NoteByIdLiveData = Repository.getNoteById(id)
        NoteByIdLiveData?.observeForever(notesObserver)
    }
}
