package com.weiting.tohealth.mygrouppage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.weiting.tohealth.data.Member
import com.weiting.tohealth.databinding.MygroupRowMemberBinding
import com.weiting.tohealth.util.Util.transferCircleImage

class GroupMemberAdapter : ListAdapter<Member, GroupMemberAdapter.GroupMemberViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<Member>() {
        override fun areItemsTheSame(oldItem: Member, newItem: Member): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: Member, newItem: Member): Boolean =
            oldItem == newItem
    }

    inner class GroupMemberViewHolder(private val binding: MygroupRowMemberBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(member: Member) {
            binding.apply {
                tvMemberName.text = member.nickName
                transferCircleImage(ivMemberImage, member.profilePhoto)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupMemberViewHolder {
        return GroupMemberViewHolder(
            MygroupRowMemberBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: GroupMemberViewHolder, position: Int) {
        return holder.bind(getItem(position))
    }
}
