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

        var count = 0

        val homeAdapter = HomeAdapter(HomeAdapter.OnclickListener {
            when (it) {
                is HomePageItem.NextTask -> {
                    //TODO 快速吃藥<依需求>
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
                        findNavController().navigate(
                            NavigationDirections.actionGlobalDrugRecordDialog(
                                it.drug.DrugData!!
                            )
                        )
                    }

                    is ItemDataType.MeasureType -> {
                        findNavController().navigate(
                            NavigationDirections.actionGlobalMeasureRecordDialog(
                                it.measure.MeasureData!!
                            )
                        )
                    }

                    is ItemDataType.ActivityType -> {
                        findNavController().navigate(
                            NavigationDirections.actionGlobalActivityRecordDialog(
                                it.activity.ActivityData!!
                            )
                        )
                    }

                    is ItemDataType.CareType -> {
                        findNavController().navigate(
                            NavigationDirections.actionGlobalCareRecordDialog(
                                it.care.CareData!!
                            )
                        )
                    }

                }
            })

        val factory = HomeViewModelFactory(PublicApplication.application.firebaseDataRepository)
        val viewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)

        //Card
        viewModel.nextTaskList.observe(viewLifecycleOwner) {

            homeAdapter.submitList(it)

        }
//
//        viewModel.itemDataTypeList.observe(viewLifecycleOwner) {
//            viewModel.getItemDataIntoHomePageItem(it)
//        }


        //Get data from firebase
        viewModel.drugList.observe(viewLifecycleOwner) {
            viewModel.getDrugs(it)
        }

        viewModel.measureList.observe(viewLifecycleOwner) {
            viewModel.getMeasures(it)
        }

        viewModel.activityList.observe(viewLifecycleOwner) {
            viewModel.getActivity(it)
        }

        viewModel.careList.observe(viewLifecycleOwner) {
            viewModel.getCares(it)
        }



        binding.apply {
            rvHomeCardView.adapter = homeAdapter
        }

        return binding.root
    }

}