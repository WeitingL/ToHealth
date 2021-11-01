package com.weiting.tohealth.mygrouppage.grouproom.board

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.weiting.tohealth.data.Note
import com.weiting.tohealth.databinding.NoteRowBinding
import com.weiting.tohealth.toFooter
import com.weiting.tohealth.toStringFromTimeStamp

class BoardNotesAdapter : ListAdapter<Note, BoardNotesAdapter.NoteViewHolder>(Diffcallback) {

    object Diffcallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean =
            newItem == oldItem

    }

    inner class NoteViewHolder(private val binding: NoteRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(note: Note){
                binding.apply {
                    tvNoteTitle.text = note.title
                    tvEditor.text = note.editor
                    tvNoteContext.text = note.content
                    tvNoteCreateTime.text = toStringFromTimeStamp(note.createTimestamp)
                    tvFooter.text = toFooter(note.footer)
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            NoteRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        return holder.bind(getItem(position))
    }

}