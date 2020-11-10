package com.lenakurasheva.notes.ui.note

import androidx.annotation.VisibleForTesting
import com.lenakurasheva.notes.data.Repository
import com.lenakurasheva.notes.data.entity.Note
import com.lenakurasheva.notes.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class NoteViewModel(val repository: Repository) : BaseViewModel<NoteData>() {

    @VisibleForTesting
    public var pendingNote: Note? = null

    fun saveChanges(note: Note) {
        pendingNote = note
    }

    fun loadNote(id: String) = launch {
        try{
            repository.getNoteById(id).let {
                setData(NoteData(note = it))
            }
        } catch (e: Throwable){
            setError(e)
        }
    }

    fun deleteNote() = launch {
       try {
           pendingNote?.let { repository.deleteNote(it.id) }
           pendingNote = null
           setData(NoteData(isDeleted = true))
       } catch (e: Throwable){
           setError(e)
       }
    }

    // Этот метод будет вызван системой при окончательном уничтожении Activity:
    @VisibleForTesting
    public override fun onCleared() {
        launch {
            pendingNote?.let {
                repository.saveNote(it)
            }
        }
    }
}
