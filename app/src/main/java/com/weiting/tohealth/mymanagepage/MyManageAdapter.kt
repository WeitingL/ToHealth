package com.weiting.tohealth.mymanagepage

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.weiting.tohealth.data.ItemType
import com.weiting.tohealth.data.User

const val ITEM_TYPE = "itemType"
const val USER = "user"
const val PRIVATE = "private"

class MyManageAdapter(fragment: Fragment, private val user: User, private val private: Int) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> getManageDetailFragment(bundleOf(ITEM_TYPE to ItemType.DRUG, USER to user, PRIVATE to private))
            1 -> getManageDetailFragment(bundleOf(ITEM_TYPE to ItemType.MEASURE, USER to user, PRIVATE to private))
            2 -> getManageDetailFragment(bundleOf(ITEM_TYPE to ItemType.EVENT, USER to user, PRIVATE to private))
            3 -> getManageDetailFragment(bundleOf(ITEM_TYPE to ItemType.CARE, USER to user, PRIVATE to private))
            else -> throw Exception("Unknown position $position")
        }
    }

    private fun getManageDetailFragment(bundle: Bundle): ManageDetailFragment {
        val fragment = ManageDetailFragment()
        fragment.arguments = bundle
        return fragment
    }
}
