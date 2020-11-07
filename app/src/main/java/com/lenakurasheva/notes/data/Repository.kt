package com.lenakurasheva.notes.data

import com.lenakurasheva.notes.data.entity.Note
import com.lenakurasheva.notes.data.provider.DataProvider

class Repository(val dataProvider: DataProvider) {

    fun getNotes() = dataProvider.subcribeToNotes()
    suspend fun saveNote(note: Note) = dataProvider.saveNote(note)
    suspend fun getNoteById(id: String) = dataProvider.getNoteById(id)
    suspend fun getCurrentUser() = dataProvider.getCurrentUser()
    suspend fun deleteNote(id: String) = dataProvider.deleteNote(id)
}
