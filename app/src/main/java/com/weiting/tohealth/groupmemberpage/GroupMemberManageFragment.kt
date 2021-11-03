package com.weiting.tohealth.groupmemberpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.weiting.tohealth.databinding.GroupMemberManagementFragmentBinding

class GroupMemberManageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = GroupMemberManagementFragmentBinding.inflate(inflater, container, false)

        val viewPager = binding.vpMemberManagement
        val tabLayout = binding.tabLayout

        viewPager.adapter = GroupMemberManageAdapter(this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "藥物項目"
                1 -> tab.text = "測量項目"
                2 -> tab.text = "活動項目"
                3 -> tab.text = "關懷項目"
            }
        }.attach()

        return binding.root
    }
}