package com.lenakurasheva.notes.data

import com.lenakurasheva.notes.data.model.Note
import com.lenakurasheva.notes.data.provider.DataProvider
import com.lenakurasheva.notes.data.provider.FirestoreDataProvider

object Repository {
    val dataProvider: DataProvider = FirestoreDataProvider()

    fun getNotes() = dataProvider.getNotes()
    fun saveNote(note: Note) = dataProvider.saveNote(note)
    fun getNoteById(id: String) = dataProvider.getNoteById(id)
}
