package com.weiting.tohealth.mygrouppage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.weiting.tohealth.NavigationDirections
import com.weiting.tohealth.PublicApplication
import com.weiting.tohealth.databinding.FragmentMygroupBinding
import com.weiting.tohealth.factory.MyGroupViewModelFactory

class MyGroupFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMygroupBinding.inflate(layoutInflater, container, false)
        val factory = MyGroupViewModelFactory(PublicApplication.application.firebaseDataRepository)
        val viewModel = ViewModelProvider(this, factory).get(MyGroupViewModel::class.java)

        val adapter = GroupAdapter(
            GroupAdapter.OnclickListener {
                when (it) {
                    is GroupPageItem.MyGroups -> {
                        findNavController().navigate(
                            NavigationDirections.actionGlobalGroupFragment(
                                it.group
                            )
                        )
                    }

                    is GroupPageItem.AddGroups -> {
                        findNavController().navigate(NavigationDirections.actionGlobalAddGroupDialog())
                    }
                }
            },
            GroupAdapter.OnclickListenerForQR {
                findNavController().navigate(NavigationDirections.actionGlobalQRCodeDialog(it))
            }
        )

        viewModel.loading.observe(viewLifecycleOwner) {
            if (it == false) {
                binding.lavLoadingMygroup.visibility = View.GONE
            }
        }

        viewModel.userData.observe(viewLifecycleOwner) {
            viewModel.getGroup(it.groupList)
        }

        viewModel.groupItemList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.rvGroupList.adapter = adapter
        return binding.root
    }

}