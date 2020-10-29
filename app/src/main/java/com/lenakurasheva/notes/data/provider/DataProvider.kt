package com.lenakurasheva.notes.data.provider

import androidx.lifecycle.LiveData
import com.lenakurasheva.notes.data.entity.Note
import com.lenakurasheva.notes.data.entity.User
import com.lenakurasheva.notes.data.model.NoteResult

interface DataProvider {
    fun getNotes(): LiveData<NoteResult>
    fun saveNote(note: Note): LiveData<NoteResult>
    fun deleteNote(id: String): LiveData<NoteResult>
    fun getNoteById(id: String): LiveData<NoteResult>
    fun getCurrentUser(): LiveData<User?>
}