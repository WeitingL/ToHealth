package com.weiting.tohealth.managepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.weiting.tohealth.R
import com.weiting.tohealth.databinding.FragmentManageBinding

class ManageFragment : Fragment(R.layout.fragment_manage) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentManageBinding.inflate(inflater, container, false)

        val viewPager = binding.vpManagerPage
        val tabLayout = binding.tlManagerItem

        viewPager.adapter = ManageAdapter(this)

        TabLayoutMediator(tabLayout, viewPager){ tab, position ->
            when (position){
                0 -> tab.text = "藥物項目"
                1 -> tab.text = "測量項目"
                2 -> tab.text = "活動項目"
                3 -> tab.text = "關懷項目"
            }
        }.attach()

        return binding.root
    }
}