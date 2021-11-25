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
            this,
            User(
                name = UserManager.UserInfo.name,
                id = UserManager.UserInfo.id,
                groupList = UserManager.UserInfo.groupList
            ),
            0
        )

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = context?.getString(R.string.DrugItem)
                1 -> tab.text = context?.getString(R.string.MeasureItem)
                2 -> tab.text = context?.getString(R.string.EventItem)
                3 -> tab.text = context?.getString(R.string.CareItem)
            }
        }.attach()

        return binding.root
    }
}
