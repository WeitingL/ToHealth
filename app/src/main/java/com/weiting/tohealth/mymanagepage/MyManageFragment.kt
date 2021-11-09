package com.weiting.tohealth.mymanagepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.weiting.tohealth.R
import com.weiting.tohealth.data.User
import com.weiting.tohealth.data.UserManager
import com.weiting.tohealth.databinding.FragmentMymanageBinding

class MyManageFragment : Fragment(R.layout.fragment_mymanage) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentMymanageBinding.inflate(inflater, container, false)

        val viewPager = binding.vpManagerPage
        val tabLayout = binding.tlManagerItem

        viewPager.adapter = MyManageAdapter(
            this, User(
                name = UserManager.UserInformation.name,
                id = UserManager.UserInformation.id,
                groupList = UserManager.UserInformation.groupList
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