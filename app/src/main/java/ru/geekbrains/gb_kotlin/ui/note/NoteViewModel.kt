package ru.geekbrains.gb_kotlin.ui.note

import androidx.lifecycle.ViewModel
import ru.geekbrains.gb_kotlin.data.Repository
import ru.geekbrains.gb_kotlin.data.model.Note

class NoteViewModel (private val repository: Repository = Repository) : ViewModel() {

    private var pendingNote: Note? = null

    fun saveChanges(note: Note) {
        pendingNote = note
    }

    // Этот метод будет вызван системой при окончательном уничтожении Activity:
    override fun onCleared() {
        pendingNote?.let {
            repository.saveNote(it)
        }
    }
}
