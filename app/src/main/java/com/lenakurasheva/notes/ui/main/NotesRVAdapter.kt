package com.lenakurasheva.notes.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.lenakurasheva.notes.common.getColorInt
import kotlinx.android.synthetic.main.item_note.view.*
import ru.geekbrains.gb_kotlin.R
import com.lenakurasheva.notes.data.entity.Note

class NotesRVAdapter (val onClickListener: ((Note) -> Unit)? = null): RecyclerView.Adapter<NotesRVAdapter.NoteViewHolder>() {

    var notes: List<Note> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(notes[position])
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTV = itemView.tv_title
        private val bodyTV = itemView.tv_body

        fun bind(note: Note) = with(note) {
            titleTV.text = title
            bodyTV.text = this.note

            (itemView as CardView).setCardBackgroundColor(color.getColorInt(itemView.context))

            itemView.setOnClickListener {
                onClickListener?.invoke(note)
            }
        }
    }
}