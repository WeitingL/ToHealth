package com.weiting.tohealth.mygrouppage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.weiting.tohealth.R
import com.weiting.tohealth.data.Note
import com.weiting.tohealth.databinding.MygroupRowNoteBinding
import com.weiting.tohealth.mygrouppage.GroupAdapter.*
import com.weiting.tohealth.util.Util.toStringFromTimeStamp

class GroupNoteAdapter() :
    ListAdapter<BoardMessage, GroupNoteAdapter.NoteViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<BoardMessage>() {

        override fun areItemsTheSame(oldItem: BoardMessage, newItem: BoardMessage): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: BoardMessage, newItem: BoardMessage): Boolean =
            oldItem == newItem

    }

    inner class NoteViewHolder(private val binding: MygroupRowNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(boardMessage: BoardMessage) {
            binding.apply {
                tvNoteTitle.text = boardMessage.title
                tvContent.text = boardMessage.content
                tvCreateMember.text = boardMessage.editor
                tvNoteCreatTime.text = toStringFromTimeStamp(boardMessage.createTime)
                tvNoteTag.apply {
                    when (boardMessage.result) {
                        7 -> {
                            text = context.getString(R.string.noteTitle)
                            background = AppCompatResources.getDrawable(context, R.drawable.tag_bg_green)
                        }
                        8 -> {
                            text = context.getString(R.string.reminder)
                            background = AppCompatResources.getDrawable(context, R.drawable.tag_bg_blue)
                        }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            MygroupRowNoteBinding.inflate(
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