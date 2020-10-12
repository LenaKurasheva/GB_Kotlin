package ru.geekbrains.gb_kotlin.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.geekbrains.gb_kotlin.R
import ru.geekbrains.gb_kotlin.data.model.Note

class NotesRVAdapter : RecyclerView.Adapter<NoteViewHolder>()  {

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
}

class NoteViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
    private val titleTV = itemView.findViewById<TextView>(R.id.tv_title)
    private val bodyTV = itemView.findViewById<TextView>(R.id.tv_body)

    fun bind(note: Note) = with(note) {
        titleTV.text = title
        bodyTV.text = this.note
        itemView.setBackgroundColor(color)
    }
}