package com.weiting.tohealth.groupmemberpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.weiting.tohealth.R
import com.weiting.tohealth.data.User
import com.weiting.tohealth.databinding.GroupMemberStatisticFragmentBinding
import com.weiting.tohealth.mystatisticpage.MyStatisticAdapter
import com.weiting.tohealth.util.Util.transferCircleImage

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
        "聊天室暱稱: ${memberInfo.nickName}".also { binding.tvMemberNickNameStatistic.text = it }
        binding.tvMemberNameStatistic.text = memberInfo.name
        transferCircleImage(binding.imPhotoStatistic, memberInfo.profilePhoto)

        viewPager.isUserInputEnabled = false
        viewPager.adapter = MyStatisticAdapter(
            this,
            User(
                id = memberInfo.userId,
                name = memberInfo.name
            )
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
