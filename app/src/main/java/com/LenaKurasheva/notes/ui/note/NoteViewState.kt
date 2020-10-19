package com.LenaKurasheva.notes.ui.note

import com.LenaKurasheva.notes.data.model.Note
import com.LenaKurasheva.notes.ui.base.BaseViewState

class NoteViewState(note: Note? = null, error: Throwable? = null): BaseViewState<Note?>(note, error) {
}