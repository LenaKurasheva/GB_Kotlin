package com.LenaKurasheva.notes.data.provider

import androidx.lifecycle.LiveData
import com.LenaKurasheva.notes.data.model.Note
import com.LenaKurasheva.notes.data.model.NoteResult

interface DataProvider {
    fun getNotes(): LiveData<NoteResult>
    fun saveNote(note: Note): LiveData<NoteResult>
    fun getNoteById(id: String): LiveData<NoteResult>
}