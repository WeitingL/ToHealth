package com.weiting.tohealth.mygrouppage.grouproom.members

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.weiting.tohealth.data.Member
import com.weiting.tohealth.databinding.MembersFragmentBinding

class MembersFragment(private val list: List<Member>) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = MembersFragmentBinding.inflate(inflater, container, false)
        val adapter = MembersAdapter()

        adapter.submitList(list)

        binding.rvMemberList.adapter = adapter
        return binding.root
    }
}