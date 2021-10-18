package com.weiting.tohealth.statisticpage

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class StatisticAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> StatisticDetailFragment(StatisticType.DRUG)
            1 -> StatisticDetailFragment(StatisticType.MEASURE)
            2 -> StatisticDetailFragment(StatisticType.ACTIVITY)
            3 -> StatisticDetailFragment(StatisticType.CARE)
            else -> throw Exception("Unknown position $position")
        }
    }
}