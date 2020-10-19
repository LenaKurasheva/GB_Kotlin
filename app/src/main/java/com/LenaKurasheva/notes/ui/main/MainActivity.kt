package com.LenaKurasheva.notes.ui.main

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import ru.geekbrains.gb_kotlin.R
import com.LenaKurasheva.notes.data.model.Note
import com.LenaKurasheva.notes.ui.base.BaseActivity
import com.LenaKurasheva.notes.ui.note.NoteActivity

class MainActivity : BaseActivity<List<Note>?, MainViewState>() {

    override val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    override val layoutRes = R.layout.activity_main
    lateinit var adapter: NotesRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rv_notes.layoutManager = GridLayoutManager(this, 2)
        adapter = NotesRVAdapter { NoteActivity.start(this, it.id) }
        rv_notes.adapter = adapter

        fab.setOnClickListener { NoteActivity.start(this) }
    }

    override fun renderData(data: List<Note>?) {
        data?.let{ adapter.notes = it}
    }
}