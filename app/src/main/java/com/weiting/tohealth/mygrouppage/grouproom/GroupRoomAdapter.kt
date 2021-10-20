package com.weiting.tohealth.mygrouppage.grouproom

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.weiting.tohealth.data.Group
import com.weiting.tohealth.mygrouppage.grouproom.board.BoardFragment
import com.weiting.tohealth.mygrouppage.grouproom.chat.ChatFragment
import com.weiting.tohealth.mygrouppage.grouproom.members.MembersFragment
import com.weiting.tohealth.mystatisticpage.StatisticDetailFragment

class GroupRoomAdapter(fragment: Fragment, private val group: Group): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> getBoardFragment(bundleOf("group" to group))
            1 -> getChatFragment(bundleOf("group" to group))
            2 -> getMembersFragment(bundleOf("member" to group.member))
            else -> throw Exception("Unknown position $position")
        }
    }

    private fun getBoardFragment(bundle: Bundle): BoardFragment{
        val fragment = BoardFragment()
        fragment.arguments = bundle
        return fragment
    }

    private fun getChatFragment(bundle: Bundle): ChatFragment{
        val fragment = ChatFragment()
        fragment.arguments = bundle
        return fragment
    }

    private fun getMembersFragment(bundle: Bundle): MembersFragment{
        val fragment = MembersFragment()
        fragment.arguments = bundle
        return fragment
    }


}