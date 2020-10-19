package com.LenaKurasheva.notes.data

import com.LenaKurasheva.notes.data.model.Note
import com.LenaKurasheva.notes.data.provider.DataProvider
import com.LenaKurasheva.notes.data.provider.FirestoreDataProvider

object Repository {
    val dataProvider: DataProvider = FirestoreDataProvider()

    fun getNotes() = dataProvider.getNotes()
    fun saveNote(note: Note) = dataProvider.saveNote(note)
    fun getNoteById(id: String) = dataProvider.getNoteById(id)
}
