package com.weiting.tohealth.mygrouppage.grouproom.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.weiting.tohealth.data.Chat
import com.weiting.tohealth.databinding.ChatroomRowMessageOthersBinding
import com.weiting.tohealth.databinding.ChatroomRowMessageSelfBinding
import com.weiting.tohealth.toTimeFromTimeStamp
import java.lang.ClassCastException

const val CHAT_VIEWTYPE_SELF = 0
const val CHAT_VIEWTYPE_OTHERS = 1

class ChatAdapter() : ListAdapter<WhoseMessage, RecyclerView.ViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<WhoseMessage>() {
        override fun areItemsTheSame(oldItem: WhoseMessage, newItem: WhoseMessage): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: WhoseMessage, newItem: WhoseMessage): Boolean =
            oldItem == newItem

    }

    inner class SelfMessageViewHolder(private val binding: ChatroomRowMessageSelfBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: Chat) {
            binding.apply {

            }
        }
    }

    inner class OthersMessageViewHolder(private val binding: ChatroomRowMessageOthersBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: Chat) {
            binding.apply {

            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is WhoseMessage.SelfMessage -> CHAT_VIEWTYPE_SELF
            is WhoseMessage.OthersMessage -> CHAT_VIEWTYPE_OTHERS
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            CHAT_VIEWTYPE_SELF -> SelfMessageViewHolder(
                ChatroomRowMessageSelfBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            CHAT_VIEWTYPE_OTHERS -> OthersMessageViewHolder(
                ChatroomRowMessageOthersBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            else -> throw ClassCastException("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {

            is SelfMessageViewHolder -> {
                val data = getItem(position) as WhoseMessage.SelfMessage
                holder.bind(data.chat)
            }

            is OthersMessageViewHolder ->{
                val data = getItem(position) as WhoseMessage.OthersMessage
                holder.bind(data.chat)
            }
        }
    }
}