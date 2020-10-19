package com.LenaKurasheva.notes.ui.note

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_note.*
import ru.geekbrains.gb_kotlin.R
import com.LenaKurasheva.notes.data.model.Color
import com.LenaKurasheva.notes.data.model.Note
import com.LenaKurasheva.notes.ui.base.BaseActivity
import java.text.SimpleDateFormat
import java.util.*

class NoteActivity : BaseActivity<Note?, NoteViewState>() {

    companion object {
        private const val EXTRA_NOTE = "note"
        private const val DATE_TIME_FORMAT = "dd.MM.yy HH:mm"

        fun start(context: Context, noteId: String? = null) = Intent(context, NoteActivity::class.java).apply {
            putExtra(EXTRA_NOTE, noteId)
            context.startActivity(this)
        }
    }

    override val viewModel: NoteViewModel by lazy {
        ViewModelProvider(this).get(NoteViewModel::class.java)
    }
    override val layoutRes = R.layout.activity_note
    private var note: Note? = null

    val textChangeListener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) = saveNote()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val noteId = intent.getStringExtra(EXTRA_NOTE)

        noteId?.let {
            viewModel.loadNote(it)
        }?: let {
            supportActionBar?.title = getString(R.string.new_note_title)
        }

        initView()
    }

    override fun renderData(data: Note?) {
        this.note = data
        supportActionBar?.title = note?.lastChanged?.let {
            SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault()).format(it)
        } ?: getString(R.string.new_note_title)

        initView()
    }

    private fun initView() {
        et_title.removeTextChangedListener(textChangeListener)
        et_body.removeTextChangedListener(textChangeListener)

        note?.let {
            et_title.setText(it.title)
            et_body.setText(it.note)

            val color = when (it.color) {
                Color.WHITE -> R.color.color_white
                Color.YELLOW -> R.color.color_yello
                Color.GREEN -> R.color.color_green
                Color.BLUE -> R.color.color_blue
                Color.RED -> R.color.color_red
                Color.VIOLET -> R.color.color_violet
            }
            toolbar.setBackgroundColor(ResourcesCompat.getColor(resources, color, null))
        }
        et_title.addTextChangedListener(textChangeListener)
        et_body.addTextChangedListener(textChangeListener)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun saveNote() {
        if (et_title.text == null || et_title.text!!.length < 3) return

        note = note?.copy(
                title = et_title.text.toString(),
                note = et_body.text.toString(),
                lastChanged = Date()
        ) ?: Note(UUID.randomUUID().toString(), et_title.text.toString(), et_body.text.toString())

        note?.let { viewModel.saveChanges(it) }
    }
}