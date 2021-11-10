package com.weiting.tohealth.mygrouppage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.weiting.tohealth.data.CalenderItem
import com.weiting.tohealth.data.Note
import com.weiting.tohealth.databinding.CardviewBottombuttonRowBinding
import com.weiting.tohealth.databinding.MygroupRowGroupBinding
import com.weiting.tohealth.toStringFromTimeStamp
import java.lang.ClassCastException

const val GROUP_VIEWTYPE_GROUP = 0
const val GROUP_VIEWTYPE_ADDGROUP = 1

class GroupAdapter(val onClickListener: OnclickListener) :
    ListAdapter<GroupPageItem, RecyclerView.ViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<GroupPageItem>() {
        override fun areItemsTheSame(oldItem: GroupPageItem, newItem: GroupPageItem): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: GroupPageItem, newItem: GroupPageItem): Boolean =
            oldItem == newItem
    }

    inner class MyGroupCardViewHolder(private val binding: MygroupRowGroupBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(myGroup: GroupPageItem.MyGroups) {
            val group = myGroup.group

            val memberAdapter = GroupMemberAdapter()
            memberAdapter.submitList(group.member)

            val noteAdapter = GroupNoteAdapter()
            noteAdapter.submitList(
                getBoardMessageList(
                    myGroup.group.notes,
                    myGroup.group.calenderItems
                )
            )

            binding.apply {
                tvGroupCode.text = group.id
                tvGroupName.text = group.groupName
                rvGroupMemberList.adapter = memberAdapter
                rvGroupNoteList.adapter = noteAdapter
                tvEnterGroup.setOnClickListener {
                    onClickListener.onClick(myGroup)
                }
            }
        }
    }

    inner class AddGroupViewHolder(private val binding: CardviewBottombuttonRowBinding) :
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
            GROUP_VIEWTYPE_GROUP -> MyGroupCardViewHolder(
                MygroupRowGroupBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )

            GROUP_VIEWTYPE_ADDGROUP -> AddGroupViewHolder(
                CardviewBottombuttonRowBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            else -> throw ClassCastException("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {

            is MyGroupCardViewHolder -> {
                holder.bind(getItem(position) as GroupPageItem.MyGroups)
            }

            is AddGroupViewHolder -> {
                holder.itemView.setOnClickListener {
                    onClickListener.onClick(getItem(position))
                }
            }
        }
    }

    class OnclickListener(val clickListener: (groupPageItem: GroupPageItem) -> Unit) {
        fun onClick(groupPageItem: GroupPageItem) = clickListener(groupPageItem)
    }

    data class BoardMessage(
        val title: String,
        val content: String,
        val createTime: Timestamp,
        val editor: String,
        val result: Int
    )

    private fun getBoardMessageList(
        noteList: List<Note>,
        calenderItemList: List<CalenderItem>
    ): List<BoardMessage> {
        val list = mutableListOf<BoardMessage>()

        when {
            noteList.isNotEmpty() && calenderItemList.isNotEmpty() -> {
                val firstNoteData = noteList.first()
                val firstCalenderItemData = calenderItemList.first()
                list.add(
                    BoardMessage(
                        title = firstNoteData.title!!,
                        content = firstNoteData.content!!,
                        createTime = firstNoteData.createdTime!!,
                        editor = firstNoteData.editor!!,
                        result = 7
                    )
                )

                list.add(
                    BoardMessage(
                        title = firstCalenderItemData.content!!,
                        content = "時間: ${toStringFromTimeStamp(firstCalenderItemData.date)}",
                        createTime = firstCalenderItemData.createdTime!!,
                        editor = firstCalenderItemData.editor!!,
                        result = 8
                    )
                )
            }

            noteList.isNotEmpty() -> {
                val firstNoteData = noteList.first()
                list.add(
                    BoardMessage(
                        title = firstNoteData.title!!,
                        content = firstNoteData.content!!,
                        createTime = firstNoteData.createdTime!!,
                        editor = firstNoteData.editor!!,
                        result = 7
                    )
                )
            }
            calenderItemList.isNotEmpty() -> {
                val firstCalenderItemData = calenderItemList.first()
                list.add(
                    BoardMessage(
                        title = firstCalenderItemData.content!!,
                        content = "時間: ${toStringFromTimeStamp(firstCalenderItemData.date)}",
                        createTime = firstCalenderItemData.createdTime!!,
                        editor = firstCalenderItemData.editor!!,
                        result = 8
                    )
                )
            }
        }

        return list
    }

}