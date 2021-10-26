package com.weiting.tohealth.mymanagepage

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
import com.weiting.tohealth.data.ItemData
import com.weiting.tohealth.databinding.MymanageItemFragmentBinding
import com.weiting.tohealth.factory.ManageDetailViewModelFactory
import com.weiting.tohealth.homepage.ItemDataType
import com.weiting.tohealth.itemeditpage.EditType

class ManageDetailFragment() : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = MymanageItemFragmentBinding.inflate(inflater, container, false)
        val manageType = arguments?.get("type") as ManageType
        val factory = ManageDetailViewModelFactory(
            PublicApplication.application.firebaseDataRepository,
            manageType
        )
        val viewModel = ViewModelProvider(this, factory).get(ManageDetailViewModel::class.java)
        val adapter = ManageDetailAdapter(manageType)

        when (manageType) {

            ManageType.DRUG -> {
                viewModel.drugList.observe(viewLifecycleOwner) {
                    val list = mutableListOf<ItemData>()
                    it.forEach {
                        list += ItemData(DrugData = it)
                    }
                    viewModel.putInDetailList(list)
                }
            }

            ManageType.MEASURE -> {
                viewModel.measureList.observe(viewLifecycleOwner) {
                    val list = mutableListOf<ItemData>()
                    it.forEach {
                        list += ItemData(MeasureData = it)
                    }
                    viewModel.putInDetailList(list)
                }
            }

            ManageType.ACTIVITY -> {
                viewModel.activityList.observe(viewLifecycleOwner) {
                    val list = mutableListOf<ItemData>()
                    it.forEach {
                        list += ItemData(ActivityData = it)
                    }
                    viewModel.putInDetailList(list)
                }
            }

            ManageType.CARE -> {
                viewModel.careList.observe(viewLifecycleOwner) {
                    val list = mutableListOf<ItemData>()
                    it.forEach {
                        list += ItemData(CareData = it)
                    }
                    viewModel.putInDetailList(list)
                }
            }
        }

        viewModel.manageDetailList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                adapter.submitList(it)
            }
        }

        binding.rvManageItems.adapter = adapter
        binding.btAddItem.setOnClickListener {
            findNavController().navigate(
                NavigationDirections.actionGlobalItemEditFragment(
                    EditType.CREATE,
                    manageType
                )
            )
        }
        return binding.root
    }

}