package com.lenakurasheva.notes.data

import com.lenakurasheva.notes.data.entity.Note
import com.lenakurasheva.notes.data.provider.DataProvider

class Repository(val dataProvider: DataProvider) {

    fun getNotes() = dataProvider.getNotes()
    fun saveNote(note: Note) = dataProvider.saveNote(note)
    fun getNoteById(id: String) = dataProvider.getNoteById(id)
    fun getCurrentUser() = dataProvider.getCurrentUser()
}
