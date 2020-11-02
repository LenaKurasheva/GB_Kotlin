package com.lenakurasheva.notes.ui.note

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.lenakurasheva.notes.data.Repository
import com.lenakurasheva.notes.data.entity.Note
import com.lenakurasheva.notes.data.model.NoteResult
import com.lenakurasheva.notes.ui.base.BaseViewModel

class NoteViewModel(val repository: Repository) : BaseViewModel<NoteViewState.Data, NoteViewState>() {

    init {
        viewStateLiveData.value = NoteViewState()
    }

    private var pendingNote: Note? = null
    var noteByIdLiveData: LiveData<NoteResult>? = null
    private var deleteLiveData: LiveData<NoteResult>? = null


    private val notesObserver = object : Observer<NoteResult> {
        override fun onChanged(result: NoteResult?) {
            when (result) {
                is NoteResult.Success<*> -> {
                    pendingNote = result.data as? Note
                    viewStateLiveData.value = NoteViewState(NoteViewState.Data(pendingNote))
                }
                is NoteResult.Error -> viewStateLiveData.value = NoteViewState(error = result.error)
            }
            noteByIdLiveData?.removeObserver(this)
        }
    }

    private val deleteObserver = object : Observer<NoteResult> {
        override fun onChanged(result: NoteResult?) {
            when (result) {
                is NoteResult.Success<*> -> viewStateLiveData.value = NoteViewState(NoteViewState.Data(isDeleted = true))
                is NoteResult.Error -> viewStateLiveData.value = NoteViewState(error = result.error)
            }
            deleteLiveData?.removeObserver(this)
        }
    }

    fun saveChanges(note: Note) {
        pendingNote = note
    }

    fun loadNote(id: String){
        noteByIdLiveData = repository.getNoteById(id)
        noteByIdLiveData?.observeForever(notesObserver)
    }

    fun deleteNote() {
        pendingNote?.let {
            deleteLiveData = repository.deleteNote(it.id)
            deleteLiveData?.observeForever(deleteObserver)
            pendingNote = null
        }
    }

    // Этот метод будет вызван системой при окончательном уничтожении Activity:
    @VisibleForTesting
    public override fun onCleared() {
        pendingNote?.let {
            repository.saveNote(it)
        }
        // убираем подписку в случае, если посреди запроса пользователь решил выйти назад, например
        noteByIdLiveData?.removeObserver(notesObserver)
        deleteLiveData?.removeObserver(deleteObserver)
    }
}
