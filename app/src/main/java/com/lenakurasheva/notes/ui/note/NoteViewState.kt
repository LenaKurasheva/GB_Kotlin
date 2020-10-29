package com.lenakurasheva.notes.ui.note

import com.lenakurasheva.notes.data.entity.Note
import com.lenakurasheva.notes.ui.base.BaseViewState

class NoteViewState(data: Data = Data(), error: Throwable? = null) : BaseViewState<NoteViewState.Data>(data, error) {
    class Data(val note: Note? = null, val isDeleted: Boolean = false)
}