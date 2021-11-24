package com.weiting.tohealth.mygrouppage.grouproom.board

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.weiting.tohealth.data.Note
import com.weiting.tohealth.databinding.NoteRowBinding
import com.weiting.tohealth.util.Util.toFooter
import com.weiting.tohealth.util.Util.toStringFromTimeStamp

class BoardNotesAdapter(val onclickListener: DeleteOnclickListener) :
    ListAdapter<Note, BoardNotesAdapter.NoteViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean =
            newItem == oldItem

    }

    inner class NoteViewHolder(private val binding: NoteRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            binding.apply {
                tvNoteTitle.text = note.title
                tvEditor.text = note.editor
                tvNoteContext.text = note.content
                tvNoteCreateTime.text = toStringFromTimeStamp(note.createdTime)
                tvFooter.text = toFooter(note.footer)
                ibDelecteNote.setOnClickListener {
                    onclickListener.onClick(note)
                }
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

    class DeleteOnclickListener(val clickListener: (note: Note) -> Unit) {
        fun onClick(note: Note) = clickListener(note)
    }

}