package com.weiting.tohealth.mymanagepage

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.weiting.tohealth.data.User
import com.weiting.tohealth.mygrouppage.grouproom.board.BoardFragment

class MyManageAdapter(fragment: Fragment, private val user: User, private val private: Int): FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> getManageDetailFragment(bundleOf("type" to ManageType.DRUG, "user" to user, "private" to private))
            1 -> getManageDetailFragment(bundleOf("type" to ManageType.MEASURE, "user" to user, "private" to private))
            2 -> getManageDetailFragment(bundleOf("type" to ManageType.ACTIVITY, "user" to user, "private" to private))
            3 -> getManageDetailFragment(bundleOf("type" to ManageType.CARE, "user" to user, "private" to private))
            else -> throw Exception("Unknown position $position")
        }
    }

    private fun getManageDetailFragment(bundle: Bundle): ManageDetailFragment {
        val fragment = ManageDetailFragment()
        fragment.arguments = bundle
        return fragment
    }
}