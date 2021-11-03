package com.weiting.tohealth.groupmemberpage

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.weiting.tohealth.mymanagepage.ManageDetailFragment
import com.weiting.tohealth.mymanagepage.ManageType

class GroupMemberManageAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> getMemberManageDetailFragment(bundleOf("type" to ManageType.DRUG))
            1 -> getMemberManageDetailFragment(bundleOf("type" to ManageType.MEASURE))
            2 -> getMemberManageDetailFragment(bundleOf("type" to ManageType.ACTIVITY))
            3 -> getMemberManageDetailFragment(bundleOf("type" to ManageType.CARE))
            else -> throw Exception("Unknown position $position")
        }
    }

    private fun getMemberManageDetailFragment(bundle: Bundle): MemberManageDetailFragment {
        val fragment = MemberManageDetailFragment()
        fragment.arguments = bundle
        return fragment
    }
}