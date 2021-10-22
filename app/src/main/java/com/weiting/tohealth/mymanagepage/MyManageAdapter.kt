package com.weiting.tohealth.mymanagepage

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.weiting.tohealth.mygrouppage.grouproom.board.BoardFragment

class MyManageAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> getManageDetailFragment(bundleOf("type" to ManageType.DRUG))
            1 -> getManageDetailFragment(bundleOf("type" to ManageType.MEASURE))
            2 -> getManageDetailFragment(bundleOf("type" to ManageType.ACTIVITY))
            3 -> getManageDetailFragment(bundleOf("type" to ManageType.MEASURE))
            else -> throw Exception("Unknown position $position")
        }
    }

    private fun getManageDetailFragment(bundle: Bundle): ManageDetailFragment {
        val fragment = ManageDetailFragment()
        fragment.arguments = bundle
        return fragment
    }
}