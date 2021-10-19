package com.weiting.tohealth.mymanagepage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.weiting.tohealth.PublicApplication
import com.weiting.tohealth.databinding.MymanageItemFragmentBinding
import com.weiting.tohealth.factory.ManageDetailViewModelFactory

class ManageDetailFragment(private val manageType: ManageType) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = MymanageItemFragmentBinding.inflate(inflater, container, false)
        val factory = ManageDetailViewModelFactory(
            PublicApplication.application.firebaseDataRepository,
            manageType
        )
        val viewModel = ViewModelProvider(this, factory).get(ManageDetailViewModel::class.java)
        val adapter = ManageDetailAdapter()

        viewModel.manageDetailList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            Log.i("work?", "$it")
        }

        binding.rvManageItems.adapter = adapter
        return binding.root
    }

}