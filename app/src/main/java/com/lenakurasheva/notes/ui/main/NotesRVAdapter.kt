package com.lenakurasheva.notes.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.lenakurasheva.notes.common.getColorInt
import ru.geekbrains.gb_kotlin.R
import com.lenakurasheva.notes.data.entity.Note
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_note.*

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

    inner class NoteViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(note: Note)  {
            with(note) {
                tv_title.text = title
                tv_body.text = this.note

                (itemView as CardView).setCardBackgroundColor(color.getColorInt(containerView.context))

                itemView.setOnClickListener {
                    onClickListener?.invoke(note)
                }
            }
        }
    }
}