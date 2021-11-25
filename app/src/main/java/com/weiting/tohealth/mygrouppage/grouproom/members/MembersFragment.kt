package com.weiting.tohealth.mygrouppage.grouproom.members

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.weiting.tohealth.NavigationDirections
import com.weiting.tohealth.PublicApplication
import com.weiting.tohealth.data.Group
import com.weiting.tohealth.databinding.MembersFragmentBinding
import com.weiting.tohealth.factory.MembersViewModelFactory
import com.weiting.tohealth.mygrouppage.grouproom.GROUP

class MembersFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = MembersFragmentBinding.inflate(inflater, container, false)
        val group = arguments?.get(GROUP) as Group
        val factory =
            MembersViewModelFactory(PublicApplication.application.firebaseDataRepository, group)
        val viewModel = ViewModelProvider(this, factory).get(MembersViewModel::class.java)
        val adapter = MembersAdapter(
            MembersAdapter.EditOnclickListener {
                findNavController().navigate(NavigationDirections.actionGlobalGroupMemberManageFragment(it, group.id?:""))
            },
            MembersAdapter.ViewOnclickListener {
                findNavController().navigate(NavigationDirections.actionGlobalGroupMemberStatisticFragment(it))
            }
        )

        viewModel.memberLive.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.apply {
            rvMemberList.adapter = adapter
        }
        return binding.root
    }
}
