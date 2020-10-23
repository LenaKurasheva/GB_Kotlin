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
    var noteByIdLiveData: LiveData<NoteResult>? = null

    private val notesObserver = object : Observer<NoteResult> {
        override fun onChanged(result: NoteResult?) {
            when (result) {
                is NoteResult.Success<*> -> viewStateLiveData.value = NoteViewState(result.data as? Note)
                is NoteResult.Error -> viewStateLiveData.value = NoteViewState(error = result.error)
            }
            noteByIdLiveData?.removeObserver(this)
        }
    }

    fun saveChanges(note: Note) {
        pendingNote = note
    }

    // Этот метод будет вызван системой при окончательном уничтожении Activity:
    override fun onCleared() {
        pendingNote?.let {
            Repository.saveNote(it)
        }
        noteByIdLiveData?.removeObserver(notesObserver)
    }

    fun loadNote(id: String){
        noteByIdLiveData = Repository.getNoteById(id)
        noteByIdLiveData?.observeForever(notesObserver)
    }
}
