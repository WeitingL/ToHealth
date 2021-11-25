package com.weiting.tohealth.mystatisticpage

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.weiting.tohealth.data.ItemType
import com.weiting.tohealth.data.User
import com.weiting.tohealth.mymanagepage.ITEM_TYPE
import com.weiting.tohealth.mymanagepage.USER

class MyStatisticAdapter(fragment: Fragment, private val user: User) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> getInstance(bundleOf(ITEM_TYPE to ItemType.DRUG, USER to user))
            1 -> getInstance(bundleOf(ITEM_TYPE to ItemType.MEASURE, USER to user))
            2 -> getInstance(bundleOf(ITEM_TYPE to ItemType.EVENT, USER to user))
            3 -> getInstance(bundleOf(ITEM_TYPE to ItemType.CARE, USER to user))
            else -> throw Exception("Unknown position $position")
        }
    }

    // Can't use the fragment constructor to transport the data, it make app crash when configuration change happens.
    private fun getInstance(bundle: Bundle): StatisticDetailFragment {
        val fragment = StatisticDetailFragment()
        fragment.arguments = bundle
        return fragment
    }
}
