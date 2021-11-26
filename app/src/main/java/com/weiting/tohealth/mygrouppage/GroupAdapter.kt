package com.weiting.tohealth.mygrouppage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.Timestamp
import com.weiting.tohealth.data.Reminder
import com.weiting.tohealth.data.Note
import com.weiting.tohealth.databinding.CardviewBottombuttonRowBinding
import com.weiting.tohealth.databinding.MygroupRowGroupBinding
import com.weiting.tohealth.util.Util.getTimeStampToDateAndTimeString
import java.lang.ClassCastException

const val VIEW_TYPE_GROUP = 0
const val VIEW_TYPE_ADD_GROUP = 1

class GroupAdapter(
    val onClickListener: OnclickListener,
    val onclickListenerForQR: OnclickListenerForQR
) :
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

            if (getBoardMessageList(
                    myGroup.group.notes, myGroup.group.reminders
                ).isEmpty()
            ) {
                binding.rvGroupNoteList.visibility = View.GONE
                binding.tabLayoutForDots.visibility = View.GONE
                binding.textView.visibility = View.GONE
            } else {
                binding.rvGroupNoteList.visibility = View.VISIBLE
                binding.tabLayoutForDots.visibility = View.VISIBLE
                binding.textView.visibility = View.VISIBLE
            }

            val noteAdapter = GroupNoteViewPagerAdapter(
                getBoardMessageList(
                    myGroup.group.notes, myGroup.group.reminders
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
                imGenerateQR.setOnClickListener {
                    onclickListenerForQR.onClickForQR(myGroup.group.id?:"")
                }

                TabLayoutMediator(tabLayoutForDots, rvGroupNoteList) { _, _ ->
                }.attach()
            }
        }
    }

    inner class AddGroupViewHolder(binding: CardviewBottombuttonRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is GroupPageItem.MyGroups -> VIEW_TYPE_GROUP
            is GroupPageItem.AddGroups -> VIEW_TYPE_ADD_GROUP
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_GROUP -> MyGroupCardViewHolder(
                MygroupRowGroupBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ),
                    parent, false
                )
            )

            VIEW_TYPE_ADD_GROUP -> AddGroupViewHolder(
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
        reminderList: List<Reminder>
    ): List<BoardMessage> {
        val list = mutableListOf<BoardMessage>()

        when {
            noteList.isNotEmpty() && reminderList.isNotEmpty() -> {
                noteList.forEach { note ->
                    list.add(
                        BoardMessage(
                            title = note.title ?: "",
                            content = note.content ?: "",
                            createTime = note.createdTime ?: Timestamp.now(),
                            editor = note.editor ?: "",
                            result = 7
                        )
                    )
                }

                reminderList.forEach { calenderItem ->
                    list.add(
                        BoardMessage(
                            title = calenderItem.content ?: "",
                            content = "時間: ${getTimeStampToDateAndTimeString(calenderItem.date)}",
                            createTime = calenderItem.createdTime ?: Timestamp.now(),
                            editor = calenderItem.editor ?: "",
                            result = 8
                        )
                    )
                }
            }

            noteList.isNotEmpty() -> {
                noteList.forEach { note ->
                    list.add(
                        BoardMessage(
                            title = note.title ?: "",
                            content = note.content ?: "",
                            createTime = note.createdTime ?: Timestamp.now(),
                            editor = note.editor!!,
                            result = 7
                        )
                    )
                }
            }
            reminderList.isNotEmpty() -> {
                reminderList.forEach { calenderItem ->
                    list.add(
                        BoardMessage(
                            title = calenderItem.content ?: "",
                            content = "時間: ${getTimeStampToDateAndTimeString(calenderItem.date)}",
                            createTime = calenderItem.createdTime ?: Timestamp.now(),
                            editor = calenderItem.editor ?: "",
                            result = 8
                        )
                    )
                }
            }
        }
        return list
    }
}
