package com.lenakurasheva.notes.ui.note

import com.lenakurasheva.notes.data.model.Note
import com.lenakurasheva.notes.ui.base.BaseViewState

class NoteViewState(note: Note? = null, error: Throwable? = null): BaseViewState<Note?>(note, error) {
}