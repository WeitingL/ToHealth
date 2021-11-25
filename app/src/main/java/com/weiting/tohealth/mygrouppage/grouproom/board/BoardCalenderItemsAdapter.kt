package com.weiting.tohealth.mygrouppage.grouproom.board

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.weiting.tohealth.data.Reminder
import com.weiting.tohealth.databinding.CalenderItemRowBinding
import com.weiting.tohealth.util.Util.toStringFromTimeStamp

class BoardCalenderItemsAdapter(val onclickListener: DeleteOnclickListener) :
    ListAdapter<Reminder, BoardCalenderItemsAdapter.CalenderItemViewHolder>(DiffCallback) {
    object DiffCallback : DiffUtil.ItemCallback<Reminder>() {

        override fun areItemsTheSame(oldItem: Reminder, newItem: Reminder): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: Reminder, newItem: Reminder): Boolean =
            oldItem == newItem
    }

    inner class CalenderItemViewHolder(private val binding: CalenderItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(reminder: Reminder) {
            binding.apply {
                tvCalenderTitle.text = reminder.content
                tvCreateTimeCalender.text = toStringFromTimeStamp(reminder.createdTime)
                tvDateCalender.text = toStringFromTimeStamp(reminder.date)
                tvEditorCalender.text = reminder.editor
                ibDeleteReminder.setOnClickListener {
                    onclickListener.onClick(reminder)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalenderItemViewHolder {
        return CalenderItemViewHolder(
            CalenderItemRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CalenderItemViewHolder, position: Int) {
        return holder.bind(getItem(position))
    }

    class DeleteOnclickListener(val clickListener: (reminder: Reminder) -> Unit) {
        fun onClick(reminder: Reminder) = clickListener(reminder)
    }
}
