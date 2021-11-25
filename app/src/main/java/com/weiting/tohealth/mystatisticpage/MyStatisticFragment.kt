package com.weiting.tohealth.mystatisticpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.weiting.tohealth.R
import com.weiting.tohealth.data.User
import com.weiting.tohealth.data.UserManager
import com.weiting.tohealth.databinding.FragmentMystatisticBinding

class MyStatisticFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMystatisticBinding.inflate(
            inflater, container, false
        )

        val viewPager = binding.vpStatistic
        val tabLayout = binding.tlStatisticItem

        viewPager.isUserInputEnabled = false
        viewPager.adapter = MyStatisticAdapter(
            this,
            User(
                name = UserManager.UserInfo.name,
                id = UserManager.UserInfo.id,
                groupList = UserManager.UserInfo.groupList
            )
        )

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.drugLogs)
                1 -> tab.text = getString(R.string.measureLogs)
                2 -> tab.text = getString(R.string.activityLogs)
                3 -> tab.text = getString(R.string.careLogs)
            }
        }.attach()

        return binding.root
    }
}
