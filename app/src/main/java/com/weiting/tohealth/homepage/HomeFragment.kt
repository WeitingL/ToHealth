package com.weiting.tohealth.homepage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.weiting.tohealth.NavigationDirections
import com.weiting.tohealth.PublicApplication
import com.weiting.tohealth.data.*
import com.weiting.tohealth.databinding.FragmentHomeBinding
import com.weiting.tohealth.factory.HomeViewModelFactory
import com.weiting.tohealth.getVmFactory
import com.weiting.tohealth.homepage.HomeViewModel.*
import com.weiting.tohealth.itemeditpage.EditType
import com.weiting.tohealth.mymanagepage.ManageType

class HomeFragment : Fragment() {

    private val viewModel by viewModels<HomeViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        val homeAdapter = HomeAdapter(
            HomeAdapter.OnclickListener {
                if (it == HomePageItem.AddNewItem) {
                    findNavController().navigate(
                        NavigationDirections.actionGlobalItemEditFragment(
                            EditType.CREATE, ManageType.DRUG
                        )
                    )
                }
            }, viewModel
        )

        //Card
        viewModel.nextTaskList.observe(viewLifecycleOwner) {
            homeAdapter.submitList(it)
            Toast.makeText(context, "取得最新資訊", Toast.LENGTH_LONG).show()
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

        //Navigation to PostLog or skip.
        viewModel.navigateToDialog.observe(viewLifecycleOwner) {
            when (it.itemDataType) {
                is ItemDataType.DrugType -> {
                    viewModel.getItemLog(
                        ItemLogData(
                            ItemId = it.itemDataType.drug.DrugData?.id,
                            ItemType = ItemType.DRUG,
                            DrugLog = DrugLog(
                                result = 0,
                                createTime = Timestamp.now()
                            )
                        ), it.positionOfItem
                    )
                    Snackbar.make(requireView(), "已經完成紀錄", Snackbar.LENGTH_LONG)
                        .setAction("回復") {
                            viewModel.removeLast()
                        }.show()
                }

                is ItemDataType.MeasureType -> {
                    findNavController().navigate(
                        NavigationDirections.actionGlobalMeasureRecordFragment(
                            it.itemDataType.measure.MeasureData!!, it.positionOfItem
                        )
                    )
                }

                is ItemDataType.ActivityType -> {
                    viewModel.getItemLog(
                        ItemLogData(
                            ItemId = it.itemDataType.activity.ActivityData?.id,
                            ItemType = ItemType.ACTIVITY,
                            ActivityLog = ActivityLog(
                                result = 0,
                                createTime = Timestamp.now()
                            )
                        ), it.positionOfItem
                    )
                    Snackbar.make(requireView(), "已經完成紀錄", Snackbar.LENGTH_LONG)
                        .setAction("回復") {
                            viewModel.removeLast()
                        }.show()
                }

                is ItemDataType.CareType -> {
                    findNavController().navigate(
                        NavigationDirections.actionGlobalCareRecordFragment(
                            it.itemDataType.care.CareData!!, it.positionOfItem
                        )
                    )
                }
            }
        }

        viewModel.navigateToSkip.observe(viewLifecycleOwner) {
            when (it.itemDataType) {
                is ItemDataType.DrugType -> {
                    viewModel.getItemLog(
                        ItemLogData(
                            ItemId = it.itemDataType.drug.DrugData?.id,
                            ItemType.DRUG,
                            DrugLog(
                                result = 1,
                                createTime = Timestamp.now()
                            )
                        ), it.positionOfItem
                    )
                }

                is ItemDataType.MeasureType -> {
                    viewModel.getItemLog(
                        ItemLogData(
                            ItemId = it.itemDataType.measure.MeasureData?.id,
                            ItemType = ItemType.MEASURE,
                            MeasureLog = MeasureLog(
                                result = 1,
                                createTime = Timestamp.now()
                            )
                        ), it.positionOfItem
                    )
                }

                is ItemDataType.ActivityType -> {
                    viewModel.getItemLog(
                        ItemLogData(
                            ItemId = it.itemDataType.activity.ActivityData?.id,
                            ItemType = ItemType.ACTIVITY,
                            ActivityLog = ActivityLog(
                                result = 1,
                                createTime = Timestamp.now()
                            )
                        ), it.positionOfItem
                    )
                }

                is ItemDataType.CareType -> {
                    viewModel.getItemLog(
                        ItemLogData(
                            ItemId = it.itemDataType.care.CareData?.id,
                            ItemType = ItemType.CARE,
                            CareLog = CareLog(
                                result = 1,
                                createTime = Timestamp.now()
                            )
                        ), it.positionOfItem
                    )
                }
            }
            Snackbar.make(requireView(), "跳過紀錄", Snackbar.LENGTH_LONG)
                .setAction("回復"){
                    viewModel.removeLast()
                }.show()
        }

        binding.apply {
            rvHomeCardView.adapter = homeAdapter
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.postRecord()
    }
}