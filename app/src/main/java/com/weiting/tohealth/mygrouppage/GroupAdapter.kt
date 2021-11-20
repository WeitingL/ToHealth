package com.weiting.tohealth.mygrouppage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.Timestamp
import com.weiting.tohealth.data.CalenderItem
import com.weiting.tohealth.data.Note
import com.weiting.tohealth.databinding.CardviewBottombuttonRowBinding
import com.weiting.tohealth.databinding.MygroupRowGroupBinding
import com.weiting.tohealth.toStringFromTimeStamp
import java.lang.ClassCastException

const val GROUP_VIEWTYPE_GROUP = 0
const val GROUP_VIEWTYPE_ADDGROUP = 1

class GroupAdapter(val onClickListener: OnclickListener, val onclickListenerForQR: OnclickListenerForQR) :
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

            val noteAdapter = GroupNoteViewPagerAdapter(
                getBoardMessageList(
                    myGroup.group.notes, myGroup.group.calenderItems
                )
            )

//            val noteAdapter = GroupNoteAdapter()
//            noteAdapter.submitList(
//                getBoardMessageList(
//                    myGroup.group.notes,
//                    myGroup.group.calenderItems
//                )
//            )

            binding.apply {
                tvGroupCode.text = group.id
                tvGroupName.text = group.groupName
                rvGroupMemberList.adapter = memberAdapter
                rvGroupNoteList.adapter = noteAdapter
                tvEnterGroup.setOnClickListener {
                    onClickListener.onClick(myGroup)
                }
                imGenerateQR.setOnClickListener {
                    onclickListenerForQR.onClickForQR(myGroup.group.id!!)
                }

                TabLayoutMediator(tabLayoutForDots, rvGroupNoteList){ tab, position ->
                }.attach()

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

    class OnclickListenerForQR(val clickListener: (groupId: String) -> Unit) {
        fun onClickForQR(groupId: String) = clickListener(groupId)
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
                noteList.forEach { note ->
                    list.add(
                        BoardMessage(
                            title = note.title!!,
                            content = note.content!!,
                            createTime = note.createdTime!!,
                            editor = note.editor!!,
                            result = 7
                        )
                    )
                }

                calenderItemList.forEach { calenderItem ->
                    list.add(
                        BoardMessage(
                            title = calenderItem.content!!,
                            content = "時間: ${toStringFromTimeStamp(calenderItem.date)}",
                            createTime = calenderItem.createdTime!!,
                            editor = calenderItem.editor!!,
                            result = 8
                        )
                    )
                }
            }

            noteList.isNotEmpty() -> {
                noteList.forEach { note ->
                    list.add(
                        BoardMessage(
                            title = note.title!!,
                            content = note.content!!,
                            createTime = note.createdTime!!,
                            editor = note.editor!!,
                            result = 7
                        )
                    )
                }
            }
            calenderItemList.isNotEmpty() -> {
                calenderItemList.forEach { calenderItem ->
                    list.add(
                        BoardMessage(
                            title = calenderItem.content!!,
                            content = "時間: ${toStringFromTimeStamp(calenderItem.date)}",
                            createTime = calenderItem.createdTime!!,
                            editor = calenderItem.editor!!,
                            result = 8
                        )
                    )
                }
            }
        }

        return list
    }

}