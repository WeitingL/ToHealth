package com.weiting.tohealth.mymanagepage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.weiting.tohealth.databinding.RowManageTimeBinding
import com.weiting.tohealth.util.Util.getTimeStampToTimeString

class ManageDetailTimeAdapter :
    ListAdapter<Timestamp, ManageDetailTimeAdapter.TimeTagViewHolder>(DiffCallBack) {

    object DiffCallBack : DiffUtil.ItemCallback<Timestamp>() {
        override fun areItemsTheSame(oldItem: Timestamp, newItem: Timestamp): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: Timestamp, newItem: Timestamp): Boolean =
            oldItem == newItem
    }

    inner class TimeTagViewHolder(private val binding: RowManageTimeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(timestamp: Timestamp) {
            binding.tvTime.text = getTimeStampToTimeString(timestamp)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeTagViewHolder {
        return TimeTagViewHolder(
            RowManageTimeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TimeTagViewHolder, position: Int) {
        return holder.bind(getItem(position))
    }
}
