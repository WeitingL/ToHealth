package com.weiting.tohealth.mystatisticpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.weiting.tohealth.data.User
import com.weiting.tohealth.data.UserManager
import com.weiting.tohealth.databinding.FragmentMystatisticBinding

class MyStatisticFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMystatisticBinding.inflate(
            inflater, container, false
        )

        val viewPager = binding.vpStatistic
        val tabLayout = binding.tlStatisticItem

        viewPager.isUserInputEnabled = false
        viewPager.adapter = MyStatisticAdapter(
            this, User(
                name = UserManager.name,
                id = UserManager.userId,
                groupList = UserManager.groupList
            )
        )

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "藥物紀錄"
                1 -> tab.text = "測量記錄"
                2 -> tab.text = "活動項目"
                3 -> tab.text = "關懷項目"
            }
        }.attach()


        return binding.root
    }

}