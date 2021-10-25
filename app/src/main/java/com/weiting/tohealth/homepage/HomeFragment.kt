package com.weiting.tohealth.homepage

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
import com.weiting.tohealth.databinding.FragmentHomeBinding
import com.weiting.tohealth.factory.HomeViewModelFactory
import com.weiting.tohealth.itemeditpage.EditType
import com.weiting.tohealth.mymanagepage.ManageType

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        val homeAdapter = HomeAdapter(HomeAdapter.OnclickListener {
            when (it) {
                is HomePageItem.NextTask -> {
                }
                is HomePageItem.AddNewItem -> {
                    findNavController().navigate(
                        NavigationDirections.actionGlobalItemEditFragment(
                            EditType.CREATE, ManageType.DRUG
                        )
                    )
                }

                else -> {
                }
            }
        },
            HomeAdapter.OnclickListenerItem {
                when (it) {
                    is ItemDataType.DrugType -> {
                        findNavController().navigate(NavigationDirections.actionGlobalDrugRecordDialog(it.drug.DrugData!!))
                    }

                    is ItemDataType.MeasureType -> {
                        findNavController().navigate(NavigationDirections.actionGlobalMeasureRecordDialog(it.measure.MeasureData!!))
                    }

                    is ItemDataType.ActivityType -> {
                        findNavController().navigate(NavigationDirections.actionGlobalActivityRecordDialog(it.activity.ActivityData!!))
                    }

                    is ItemDataType.CareType -> {
                        findNavController().navigate(NavigationDirections.actionGlobalCareRecordDialog(it.care.CareData!!))
                    }

                }
            })

        val factory = HomeViewModelFactory(PublicApplication.application.firebaseDataRepository)
        val viewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)

        //Card
        viewModel.nextTaskList.observe(viewLifecycleOwner) {
            homeAdapter.submitList(it)
        }

        //Get data from firebase
        viewModel.itemDataTypeList.observe(viewLifecycleOwner) {
            viewModel.getItemDataIntoHomePageItem(it)
//            Log.i("List", it.toString())
        }

        viewModel.drugList.observe(viewLifecycleOwner){
            Log.i("drugList", it.toString())
        }

        viewModel.measureList.observe(viewLifecycleOwner){
            Log.i("measureList", it.toString())
        }

        viewModel.activityList.observe(viewLifecycleOwner){
            Log.i("activityList", it.toString())
        }

        viewModel.careList.observe(viewLifecycleOwner){
            Log.i("careList", it.toString())
        }

        binding.apply {
            rvHomeCardView.adapter = homeAdapter
        }

        return binding.root
    }

}