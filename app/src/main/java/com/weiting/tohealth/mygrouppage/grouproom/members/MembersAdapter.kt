package com.weiting.tohealth.mygrouppage.grouproom.members

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.weiting.tohealth.data.Member
import com.weiting.tohealth.databinding.MemberRowBinding

class MembersAdapter(val onClickListener: EditOnclickListener, val onclickListener: ViewOnclickListener) :
    ListAdapter<Member, MembersAdapter.MemberViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<Member>() {
        override fun areItemsTheSame(oldItem: Member, newItem: Member): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: Member, newItem: Member): Boolean =
            oldItem == newItem

    }

    inner class MemberViewHolder(private val binding: MemberRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(member: Member) {
            binding.apply {
                tvMemberName.text = member.name
                tvNickName.text = member.nickName

                btEditMember.setOnClickListener {
                    onClickListener.onClick(member)
                }
                btStastisticMember.setOnClickListener {
                    onclickListener.onClick(member)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        return MemberViewHolder(
            MemberRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        return holder.bind(getItem(position))
    }

    class EditOnclickListener(val clickListener: (member: Member) -> Unit) {
        fun onClick(member: Member) = clickListener(member)
    }

    class ViewOnclickListener(val clickListener: (member: Member) -> Unit) {
        fun onClick(member: Member) = clickListener(member)
    }

}