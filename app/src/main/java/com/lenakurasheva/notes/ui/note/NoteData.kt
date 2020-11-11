package com.lenakurasheva.notes.ui.note

import com.lenakurasheva.notes.data.entity.Note

data class NoteData(val note: Note? = null, val isDeleted: Boolean = false)
