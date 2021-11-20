package com.weiting.tohealth.groupmemberpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.weiting.tohealth.data.User
import com.weiting.tohealth.databinding.GroupMemberStatisticFragmentBinding
import com.weiting.tohealth.mystatisticpage.MyStatisticAdapter
import com.weiting.tohealth.mystatisticpage.StatisticDetailAdapter
import com.weiting.tohealth.transferCircleImage

class GroupMemberStatisticFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = GroupMemberStatisticFragmentBinding.inflate(inflater, container, false)
        val memberInfo = GroupMemberStatisticFragmentArgs.fromBundle(requireArguments()).memberInfo
        val viewPager = binding.vpMemberStatistic
        val tabLayout = binding.tabLayout
        binding.tvMemberNickNameStatistic.text = "聊天室暱稱: ${memberInfo.nickName}"
        binding.tvMemberNameStatistic.text = memberInfo.name
        transferCircleImage(binding.imageView5, memberInfo.profilePhoto)

        viewPager.isUserInputEnabled = false
        viewPager.adapter = MyStatisticAdapter(
            this, User(
                id = memberInfo.userId,
                name = memberInfo.name
            )
        )

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