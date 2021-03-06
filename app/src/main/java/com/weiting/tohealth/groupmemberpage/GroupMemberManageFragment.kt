package com.weiting.tohealth.groupmemberpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.weiting.tohealth.NavigationDirections
import com.weiting.tohealth.R
import com.weiting.tohealth.data.User
import com.weiting.tohealth.data.UserManager
import com.weiting.tohealth.databinding.GroupMemberManagementFragmentBinding
import com.weiting.tohealth.mymanagepage.MyManageAdapter
import com.weiting.tohealth.util.Util.transferCircleImage

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
        "聊天室暱稱: ${memberInfo.nickName}".also { binding.tvMemberNickName.text = it }
        transferCircleImage(binding.imPhoto, memberInfo.profilePhoto)

        if (memberInfo.userId != UserManager.UserInfo.id) {
            binding.ibEditNickName.visibility = View.GONE
        }

        // Give the OwnMemberInfo
        binding.ibEditNickName.setOnClickListener {
            findNavController().navigate(
                NavigationDirections.actionGlobalEditMyNickNameDialog(memberInfo, groupId))
        }

        viewPager.adapter = MyManageAdapter(
            this,
            User(
                id = memberInfo.userId,
                name = memberInfo.name,
            ),
            memberInfo.private?:0
        )

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.DrugItem)
                1 -> tab.text = getString(R.string.MeasureItem)
                2 -> tab.text = getString(R.string.EventItem)
                3 -> tab.text = getString(R.string.CareItem)
            }
        }.attach()

        return binding.root
    }
}
