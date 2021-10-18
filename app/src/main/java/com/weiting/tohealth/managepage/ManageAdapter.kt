package com.weiting.tohealth.managepage

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.weiting.tohealth.statisticpage.StatisticDetailFragment
import com.weiting.tohealth.statisticpage.StatisticType

class ManageAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> ManageDetailFragment(ManageType.DRUG)
            1 -> ManageDetailFragment(ManageType.MEASURE)
            2 -> ManageDetailFragment(ManageType.ACTIVITY)
            3 -> ManageDetailFragment(ManageType.CARE)
            else -> throw Exception("Unknown position $position")
        }
    }

}