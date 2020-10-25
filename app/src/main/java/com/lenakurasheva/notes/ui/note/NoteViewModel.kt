package com.lenakurasheva.notes.ui.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.lenakurasheva.notes.data.Repository
import com.lenakurasheva.notes.data.entity.Note
import com.lenakurasheva.notes.data.model.NoteResult
import com.lenakurasheva.notes.ui.base.BaseViewModel

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
            // убираем подписку сразу после того, как получен результат, т.к. noteByIdLiveData - одноразовая
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
        // убираем подписку в случае, если посреди запроса пользователь решил выйти назад, например
        noteByIdLiveData?.removeObserver(notesObserver)
    }

    fun loadNote(id: String){
        noteByIdLiveData = Repository.getNoteById(id)
        noteByIdLiveData?.observeForever(notesObserver)
    }
}
