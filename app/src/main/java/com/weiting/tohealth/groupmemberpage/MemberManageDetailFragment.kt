package com.weiting.tohealth.groupmemberpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.weiting.tohealth.databinding.MymanageItemFragmentBinding

class MemberManageDetailFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = MymanageItemFragmentBinding.inflate(inflater, container, false)


        return binding.root
    }

}