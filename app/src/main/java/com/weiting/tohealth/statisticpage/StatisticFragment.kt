package com.weiting.tohealth.statisticpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.weiting.tohealth.R
import com.weiting.tohealth.databinding.FragmentStatisticBinding

class StatisticFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentStatisticBinding.inflate(
            inflater, container, false
        )

        val viewPager = binding.vpStatistic
        val tabLayout = binding.tlStatisticItem

        viewPager.adapter = StatisticAdapter(this)

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