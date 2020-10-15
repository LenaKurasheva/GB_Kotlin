package ru.geekbrains.gb_kotlin.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.geekbrains.gb_kotlin.data.model.Color
import ru.geekbrains.gb_kotlin.data.model.Note
import java.util.*

object Repository {

    private val notesLiveData = MutableLiveData<List<Note>>()

    private val notes: MutableList<Note> = mutableListOf(
            Note( UUID.randomUUID().toString(),
                    "Моя первая заметка",
                    "Очень важная заметка. Не забыть купить молоко.",
                    Color.WHITE),
            Note( UUID.randomUUID().toString(),
                    "Моя вторая заметка",
                    "Очень важная заметка. Не забыть купить молоко.",
                    Color.BLUE),
            Note( UUID.randomUUID().toString(),
                    "Моя третья заметка",
                    "Очень важная заметка. Не забыть купить молоко.",
                    Color.GREEN),
            Note( UUID.randomUUID().toString(),
                    "Моя четвертая заметка",
                    "Очень важная заметка. Не забыть купить молоко.",
                    Color.RED),
            Note( UUID.randomUUID().toString(),
                    "Моя пятая заметка",
                    "Очень важная заметка. Не забыть купить молоко.",
                    Color.VIOLET),
            Note( UUID.randomUUID().toString(),
                    "Моя шестая заметка",
                    "Очень важная заметка. Не забыть купить молоко.",
                    Color.YELLOW),
            Note( UUID.randomUUID().toString(),
                    "Моя седьмая заметка",
                    "Очень важная заметка. Не забыть купить молоко.",
                    Color.BLUE))

    init {
        notesLiveData.value = notes
    }

    fun getNotes(): LiveData<List<Note>> {
        return notesLiveData
    }

    fun saveNote(note: Note) {
        addOrReplace(note)
        notesLiveData.value = notes
    }

    private fun addOrReplace(note: Note) {

        for (i in 0 until notes.size) {
            if (notes[i] == note) {
                notes.set(i, note)
                return
            }
        }

        notes.add(note)
    }
}
