package com.weiting.tohealth.grouppage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.weiting.tohealth.data.Group
import com.weiting.tohealth.databinding.GroupRowAddgroupBinding
import com.weiting.tohealth.databinding.GroupRowGroupBinding
import java.lang.ClassCastException

const val GROUP_VIEWTYPE_GROUP = 0
const val GROUP_VIEWTYPE_ADDGROUP = 1

class GroupAdapter (val onClickListener: OnclickListener) : ListAdapter<GroupPageItem, RecyclerView.ViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<GroupPageItem>() {
        override fun areItemsTheSame(oldItem: GroupPageItem, newItem: GroupPageItem): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: GroupPageItem, newItem: GroupPageItem): Boolean =
            oldItem == newItem
    }

    inner class GroupCardViewHolder(private val binding: GroupRowGroupBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(myGroup: GroupPageItem.MyGroups) {
            val group = myGroup.group

            val memberAdapter = GroupMemberAdapter()
            val noteAdapter = GroupNoteAdapter()

            memberAdapter.submitList(group.member)
            noteAdapter.submitList(group.note)

            binding.apply {
                tvGroupCode.text = group.code
                tvGroupName.text = group.name
                rvGroupMemberList.adapter = memberAdapter
                rvGroupNoteList.adapter = noteAdapter
            }
        }
    }

    inner class AddNewGroupViewHolder(private val binding: GroupRowAddgroupBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is GroupPageItem.MyGroups -> GROUP_VIEWTYPE_GROUP
            is GroupPageItem.AddGroups -> GROUP_VIEWTYPE_ADDGROUP
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            GROUP_VIEWTYPE_GROUP -> GroupCardViewHolder(
                GroupRowGroupBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )

            GROUP_VIEWTYPE_ADDGROUP -> AddNewGroupViewHolder(
                GroupRowAddgroupBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            else -> throw ClassCastException("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {

            is GroupCardViewHolder -> {
                holder.bind(getItem(position) as GroupPageItem.MyGroups)
                holder.itemView.setOnClickListener {
                    onClickListener.onClick(getItem(position))
                }
            }

            is AddNewGroupViewHolder -> {
                holder.itemView.setOnClickListener {
                    onClickListener.onClick(getItem(position))
                }
            }
        }
    }

    class OnclickListener(val clickListener: (groupPageItem:GroupPageItem) -> Unit) {
        fun onClick(groupPageItem:GroupPageItem) = clickListener(groupPageItem)
    }

}