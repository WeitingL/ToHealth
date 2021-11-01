package com.weiting.tohealth.mygrouppage.grouproom.members

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.weiting.tohealth.PublicApplication
import com.weiting.tohealth.data.Group
import com.weiting.tohealth.data.Member
import com.weiting.tohealth.databinding.MembersFragmentBinding
import com.weiting.tohealth.factory.MembersViewModelFactory

class MembersFragment() : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = MembersFragmentBinding.inflate(inflater, container, false)
        val group = arguments?.get("group") as Group
        val factory =
            MembersViewModelFactory(PublicApplication.application.firebaseDataRepository, group)
        val viewModel = ViewModelProvider(this, factory).get(MembersViewModel::class.java)
        val adapter = MembersAdapter()

        viewModel.liveMembersList.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }

        binding.rvMemberList.adapter = adapter
        return binding.root
    }
}