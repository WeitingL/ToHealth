package com.weiting.tohealth.groupmemberpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.weiting.tohealth.NavigationDirections
import com.weiting.tohealth.data.User
import com.weiting.tohealth.data.UserManager
import com.weiting.tohealth.databinding.GroupMemberManagementFragmentBinding
import com.weiting.tohealth.mymanagepage.MyManageAdapter

class GroupMemberManageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = GroupMemberManagementFragmentBinding.inflate(inflater, container, false)
        val memberInfo = GroupMemberManageFragmentArgs.fromBundle(requireArguments()).memberInfo
        val groupId = GroupMemberManageFragmentArgs.fromBundle(requireArguments()).groupId
        val viewPager = binding.vpMemberManagement
        val tabLayout = binding.tabLayout

        binding.tvMemberNameManage.text = memberInfo.name
        binding.tvMemberNickName.text = memberInfo.nickName

        if (memberInfo.userId != UserManager.UserInformation.id){
            binding.ibEditNickName.visibility = View.GONE
        }

        //Give the OwnMemberInfo
        binding.ibEditNickName.setOnClickListener {
            findNavController().navigate(NavigationDirections.actionGlobalEditMyNickNameDialog(memberInfo, groupId))
        }

        viewPager.adapter = MyManageAdapter(this, User(
            id = memberInfo.userId,
            name = memberInfo.name,
        ))

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