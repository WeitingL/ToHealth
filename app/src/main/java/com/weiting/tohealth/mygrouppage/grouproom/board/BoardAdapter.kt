package com.weiting.tohealth.mygrouppage.grouproom.board

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.weiting.tohealth.data.CalenderItem
import com.weiting.tohealth.data.Note
import com.weiting.tohealth.databinding.BoardRowCalenderitemBinding
import com.weiting.tohealth.databinding.BoardRowNoteBinding
import java.lang.ClassCastException

const val BOARD_VIEW_TYPE_NOTE = 0
const val BOARD_VIEW_TYPE_CALENDER = 1

class BoardAdapter(val viewModel: BoardViewModel) :
    ListAdapter<BoardType, RecyclerView.ViewHolder>(DiffCallBack) {

    object DiffCallBack : DiffUtil.ItemCallback<BoardType>() {
        override fun areItemsTheSame(oldItem: BoardType, newItem: BoardType): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: BoardType, newItem: BoardType): Boolean =
            oldItem == newItem

    }

    inner class NotesViewHolder(private val binding: BoardRowNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(list: List<Note>) {
            val adapter = BoardNotesAdapter(BoardNotesAdapter.DeleteOnclickListener {
                viewModel.deleteNote(it)
            })
            adapter.submitList(list)
            binding.rvNotes.adapter = adapter
        }
    }

    inner class CalenderViewHolder(private val binding: BoardRowCalenderitemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(list: List<CalenderItem>) {
            val adapter =
                BoardCalenderItemsAdapter(BoardCalenderItemsAdapter.DeleteOnclickListener {
                    viewModel.deleteReminder(it)
                })
            adapter.submitList(list)
            binding.rvCalenderItems.adapter = adapter
        }

    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is BoardType.Notes -> BOARD_VIEW_TYPE_NOTE
            is BoardType.CalenderItems -> BOARD_VIEW_TYPE_CALENDER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            BOARD_VIEW_TYPE_NOTE -> NotesViewHolder(
                BoardRowNoteBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )

            BOARD_VIEW_TYPE_CALENDER -> CalenderViewHolder(
                (BoardRowCalenderitemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ))
            )

            else -> throw ClassCastException("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NotesViewHolder -> {
                holder.bind((getItem(position) as BoardType.Notes).list)
            }

            is CalenderViewHolder -> {
                holder.bind((getItem(position) as BoardType.CalenderItems).list)
            }
        }
    }
}