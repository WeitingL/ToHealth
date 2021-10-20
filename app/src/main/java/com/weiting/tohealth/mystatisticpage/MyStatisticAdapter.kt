package com.weiting.tohealth.mystatisticpage

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import io.grpc.internal.DnsNameResolver

class MyStatisticAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> getInstance(bundleOf("type" to StatisticType.DRUG))
            1 -> getInstance(bundleOf("type" to StatisticType.MEASURE))
            2 -> getInstance(bundleOf("type" to StatisticType.ACTIVITY))
            3 -> getInstance(bundleOf("type" to StatisticType.CARE))
            else -> throw Exception("Unknown position $position")
        }
    }

    //Can't use the fragment constructor to transport the data, it make app crash when configuration change happens.
    private fun getInstance(bundle: Bundle): StatisticDetailFragment {
        val fragment = StatisticDetailFragment()
        fragment.arguments = bundle
        return fragment
    }
}