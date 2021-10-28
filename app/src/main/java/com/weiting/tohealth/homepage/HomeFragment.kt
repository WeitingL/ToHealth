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
        val factory = HomeViewModelFactory(PublicApplication.application.firebaseDataRepository)
        val viewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)

        val homeAdapter = HomeAdapter(
            HomeAdapter.OnclickListener {
                when (it) {
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
            }, viewModel
        )

        //Card
        viewModel.nextTaskList.observe(viewLifecycleOwner) {

            homeAdapter.submitList(it)

        }

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

        //Navigation to Dialog
        viewModel.navigateToDialog.observe(viewLifecycleOwner) {
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
        }

        viewModel.navigateToSkip.observe(viewLifecycleOwner) {
            when (it) {
                is ItemDataType.DrugType -> {
                    findNavController().navigate(
                        NavigationDirections.actionGlobalSkipRecordDialog(it.drug)
                    )
                }

                is ItemDataType.MeasureType -> {
                    findNavController().navigate(
                        NavigationDirections.actionGlobalSkipRecordDialog(
                            it.measure
                        )
                    )
                }

                is ItemDataType.ActivityType -> {
                    findNavController().navigate(
                        NavigationDirections.actionGlobalSkipRecordDialog(
                            it.activity
                        )
                    )
                }

                is ItemDataType.CareType -> {
                    findNavController().navigate(
                        NavigationDirections.actionGlobalSkipRecordDialog(
                            it.care
                        )
                    )
                }
            }
        }

        binding.apply {
            rvHomeCardView.adapter = homeAdapter
        }

        return binding.root
    }

}