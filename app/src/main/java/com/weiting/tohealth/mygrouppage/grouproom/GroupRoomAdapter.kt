package com.weiting.tohealth.mygrouppage.grouproom

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.weiting.tohealth.data.Group
import com.weiting.tohealth.mygrouppage.grouproom.board.BoardFragment
import com.weiting.tohealth.mygrouppage.grouproom.chat.ChatFragment
import com.weiting.tohealth.mygrouppage.grouproom.members.MembersFragment

class GroupRoomAdapter(fragment: Fragment, private val group: Group): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> BoardFragment(group)
            1 -> ChatFragment()
            2 -> MembersFragment(group.member)
            else -> throw Exception("Unknown position $position")
        }
    }

}