package com.weiting.tohealth.mygrouppage.grouproom.members

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.weiting.tohealth.PublicApplication
import com.weiting.tohealth.data.Member
import com.weiting.tohealth.databinding.MemberRowBinding
import com.weiting.tohealth.util.Util.transferCircleImage

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
                "聊天室暱稱: ${member.nickName}".also { tvNickName.text = it }

                transferCircleImage(ivPhoto, member.profilePhoto)

                btEditMember.setOnClickListener {
                    val context = PublicApplication.application.applicationContext
                    when(member.private){
                        1 -> Toast.makeText(context, "使用者拒絕群組編輯", Toast.LENGTH_LONG).show()
                        3 -> Toast.makeText(context, "使用者拒絕群組編輯", Toast.LENGTH_LONG).show()
                        else -> onClickListener.onClick(member)
                    }

                }
                btStastisticMember.setOnClickListener {
                    val context = PublicApplication.application.applicationContext
                    when(member.private){
                        3 -> Toast.makeText(context, "使用者拒絕群組查看", Toast.LENGTH_LONG).show()
                        else -> onclickListener.onClick(member)
                    }

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