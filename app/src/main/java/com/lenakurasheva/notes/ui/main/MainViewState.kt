package com.lenakurasheva.notes.ui.main

import com.lenakurasheva.notes.data.model.Note
import com.lenakurasheva.notes.ui.base.BaseViewState

class MainViewState(val notes: List<Note>? = null, error: Throwable? = null): BaseViewState<List<Note>?>(notes, error)