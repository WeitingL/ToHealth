package com.weiting.tohealth.mygrouppage.grouproom.board

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.weiting.tohealth.data.CalenderItem
import com.weiting.tohealth.databinding.CalenderItemRowBinding
import com.weiting.tohealth.util.Util.toStringFromTimeStamp

class BoardCalenderItemsAdapter(val onclickListener: DeleteOnclickListener) :
    ListAdapter<CalenderItem, BoardCalenderItemsAdapter.CalenderItemViewHolder>(DiffCallback) {
    object DiffCallback : DiffUtil.ItemCallback<CalenderItem>() {

        override fun areItemsTheSame(oldItem: CalenderItem, newItem: CalenderItem): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: CalenderItem, newItem: CalenderItem): Boolean =
            oldItem == newItem
    }

    inner class CalenderItemViewHolder(private val binding: CalenderItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(calenderItem: CalenderItem) {
            binding.apply {
                tvCalenderTitle.text = calenderItem.content
                tvCreateTimeCalender.text = toStringFromTimeStamp(calenderItem.createdTime)
                tvDateCalender.text = toStringFromTimeStamp(calenderItem.date)
                tvEditorCalender.text = calenderItem.editor
                ibDeleteReminder.setOnClickListener {
                    onclickListener.onClick(calenderItem)
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

    class DeleteOnclickListener(val clickListener: (calenderItem: CalenderItem) -> Unit) {
        fun onClick(calenderItem: CalenderItem) = clickListener(calenderItem)
    }
}
