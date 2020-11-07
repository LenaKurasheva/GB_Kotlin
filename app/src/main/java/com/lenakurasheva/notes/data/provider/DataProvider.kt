package com.lenakurasheva.notes.data.provider

import com.lenakurasheva.notes.data.entity.Note
import com.lenakurasheva.notes.data.entity.User
import com.lenakurasheva.notes.data.model.NoteResult
import kotlinx.coroutines.channels.ReceiveChannel

interface DataProvider {
    fun subcribeToNotes(): ReceiveChannel<NoteResult>
    suspend fun saveNote(note: Note): Note
    suspend fun deleteNote(id: String)
    suspend fun getNoteById(id: String): Note
    suspend fun getCurrentUser(): User?
}