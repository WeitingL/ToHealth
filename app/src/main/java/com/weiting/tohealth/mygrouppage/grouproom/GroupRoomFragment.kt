package com.weiting.tohealth.mygrouppage.grouproom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.weiting.tohealth.databinding.GroupFragmentBinding

class GroupRoomFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = GroupFragmentBinding.inflate(inflater, container, false)
        val group = GroupRoomFragmentArgs.fromBundle(requireArguments()).group

        val tabLayout = binding.tlGroupInfo
        val viewPager = binding.vpGroupFragment

        viewPager.adapter = GroupRoomAdapter(this, group)

        TabLayoutMediator(tabLayout, viewPager){ tab, position ->
            when (position){
                0 -> tab.text = "看板"
                1 -> tab.text = "聊天室"
                2 -> tab.text = "成員"
            }
        }.attach()

        return binding.root
    }

}