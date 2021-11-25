package com.weiting.tohealth.alertmessagepage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.weiting.tohealth.databinding.AlertmessageRowBinding

class AlterMessageAdapter :
    ListAdapter<AlterMessageRecord, AlterMessageAdapter.AlterMessageViewHolder>(
        DiffCallBack
    ) {

    object DiffCallBack : DiffUtil.ItemCallback<AlterMessageRecord>() {
        override fun areItemsTheSame(
            oldItem: AlterMessageRecord,
            newItem: AlterMessageRecord
        ): Boolean = oldItem === newItem

        override fun areContentsTheSame(
            oldItem: AlterMessageRecord,
            newItem: AlterMessageRecord
        ): Boolean = oldItem == newItem
    }

    inner class AlterMessageViewHolder(private val binding: AlertmessageRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(alterMessageRecord: AlterMessageRecord) {
            binding.notificationRecord = alterMessageRecord
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlterMessageViewHolder {
        return AlterMessageViewHolder(
            AlertmessageRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: AlterMessageViewHolder, position: Int) {
        return holder.bind(getItem(position))
    }
}
