package com.LenaKurasheva.notes.ui.main

import com.LenaKurasheva.notes.data.model.Note
import com.LenaKurasheva.notes.ui.base.BaseViewState

class MainViewState(val notes: List<Note>? = null, error: Throwable? = null): BaseViewState<List<Note>?>(notes, error)