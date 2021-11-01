package com.weiting.tohealth.itemeditpage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.weiting.tohealth.databinding.EditTimeRowBinding
import com.weiting.tohealth.homepage.HomeAdapter
import com.weiting.tohealth.homepage.HomePageItem
import com.weiting.tohealth.toStringFromTimeStamp
import com.weiting.tohealth.toTimeFromTimeStamp

class TimeSetAdapter(val onClickListener: OnclickListener) : ListAdapter<Timestamp, TimeSetAdapter.TimeEditViewHolder>(DiffCallBack) {

    object DiffCallBack : DiffUtil.ItemCallback<Timestamp>() {
        override fun areItemsTheSame(oldItem: Timestamp, newItem: Timestamp): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Timestamp, newItem: Timestamp): Boolean =
            oldItem == newItem

    }

    inner class TimeEditViewHolder(private val binding: EditTimeRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(timestamp: Timestamp, position: Int) {
            binding.tvTimerSet.text = toTimeFromTimeStamp(timestamp)
            binding.btRemoveTimeSet.setOnClickListener {
                onClickListener.onClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeEditViewHolder {
        return TimeEditViewHolder(
            EditTimeRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TimeEditViewHolder, position: Int) {
        holder.bind(getItem(position), position)

    }

    class OnclickListener(val clickListener: (position: Int) -> Unit) {
        fun onClick(position: Int) = clickListener(position)
    }

}