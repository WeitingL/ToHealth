package com.weiting.tohealth.grouppage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.weiting.tohealth.data.Member
import com.weiting.tohealth.data.Note
import com.weiting.tohealth.databinding.GroupRowMemberBinding
import com.weiting.tohealth.databinding.GroupRowNoteBinding
import com.weiting.tohealth.grouppage.GroupNoteAdapter.NoteViewHolder

class GroupNoteAdapter() : ListAdapter<Note, NoteViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean =
            oldItem == newItem
    }

    inner class NoteViewHolder(private val binding: GroupRowNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            binding.apply {
                tvNoteTitle.text = note.title
                tvContent.text = note.content
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            GroupRowNoteBinding.inflate(
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