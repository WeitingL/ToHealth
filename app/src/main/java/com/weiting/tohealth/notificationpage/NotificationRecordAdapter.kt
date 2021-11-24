package com.weiting.tohealth.notificationpage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.weiting.tohealth.databinding.NotificationRowBinding

class NotificationRecordAdapter :
    ListAdapter<NotificationRecord, NotificationRecordAdapter.NotificationRecordViewHolder>(
        DiffCallBack
    ) {

    object DiffCallBack : DiffUtil.ItemCallback<NotificationRecord>() {
        override fun areItemsTheSame(
            oldItem: NotificationRecord,
            newItem: NotificationRecord
        ): Boolean = oldItem === newItem

        override fun areContentsTheSame(
            oldItem: NotificationRecord,
            newItem: NotificationRecord
        ): Boolean = oldItem == newItem
    }

    inner class NotificationRecordViewHolder(private val binding: NotificationRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(notificationRecord: NotificationRecord) {
            binding.notificationRecord = notificationRecord
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationRecordViewHolder {
        return NotificationRecordViewHolder(
            NotificationRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: NotificationRecordViewHolder, position: Int) {
        return holder.bind(getItem(position))
    }
}
