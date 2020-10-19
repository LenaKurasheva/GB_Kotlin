package com.LenaKurasheva.notes.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_note.view.*
import ru.geekbrains.gb_kotlin.R
import com.LenaKurasheva.notes.data.model.Color
import com.LenaKurasheva.notes.data.model.Note

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

            val color = when (color) {
                Color.WHITE -> R.color.color_white
                Color.YELLOW -> R.color.color_yello
                Color.GREEN -> R.color.color_green
                Color.BLUE -> R.color.color_blue
                Color.RED -> R.color.color_red
                Color.VIOLET -> R.color.color_violet
            }
            (itemView as CardView).setCardBackgroundColor(ResourcesCompat.getColor(itemView.context.resources, color, null))

            itemView.setOnClickListener {
                onClickListener?.invoke(note)
            }
        }
    }
}