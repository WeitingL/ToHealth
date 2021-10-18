package com.weiting.tohealth.managepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.weiting.tohealth.databinding.ManageDetailFragmentBinding

class ManageDetailFragment(manageType: ManageType): Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = ManageDetailFragmentBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this).get(ManageDetailViewModel::class.java)
        val adapter = ManageDetailAdapter()

        viewModel.manageDetailList.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }

        binding.rvManageItems.adapter = adapter
        return binding.root
    }

}