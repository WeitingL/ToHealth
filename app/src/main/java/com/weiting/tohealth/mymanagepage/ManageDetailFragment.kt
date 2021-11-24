package com.weiting.tohealth.mymanagepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.weiting.tohealth.NavigationDirections
import com.weiting.tohealth.PublicApplication
import com.weiting.tohealth.data.ItemData
import com.weiting.tohealth.data.User
import com.weiting.tohealth.databinding.MymanageItemFragmentBinding
import com.weiting.tohealth.factory.ManageDetailViewModelFactory

class ManageDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = MymanageItemFragmentBinding.inflate(inflater, container, false)
        val manageType = arguments?.get("type") as ManageType
        val user = arguments?.get("user") as User
        val private = arguments?.get("private") as Int
        val factory = ManageDetailViewModelFactory(
            PublicApplication.application.firebaseDataRepository,
            manageType,
            user
        )
        val viewModel = ViewModelProvider(this, factory).get(ManageDetailViewModel::class.java)

        if (private == 2) {
            binding.btAddItem.visibility = View.GONE
        }

        val adapter = ManageDetailAdapter(
            manageType,
            ManageDetailAdapter.OnclickListener { itemData, itemType ->
                when (private == 2) {
                    true -> Toast.makeText(context, "使用者限制您的編輯", Toast.LENGTH_LONG).show()
                    false -> {
                        findNavController().navigate(
                            NavigationDirections.actionGlobalItemUpdateFragment(
                                itemData = itemData,
                                manageType = manageType,
                                userInfo = user
                            )
                        )
                    }
                }
            }
        )

        when (manageType) {
            ManageType.DRUG -> {
                viewModel.drugList.observe(viewLifecycleOwner) {
                    val list = mutableListOf<ItemData>()
                    val newList = it.filter {
                        it.status != 2
                    }

                    newList.forEach {
                        list += ItemData(DrugData = it)
                    }
                    viewModel.putInDetailList(list)
                }
            }

            ManageType.MEASURE -> {
                viewModel.measureList.observe(viewLifecycleOwner) {
                    val list = mutableListOf<ItemData>()
                    val newList = it.filter {
                        it.status != 2
                    }
                    newList.forEach {
                        list += ItemData(MeasureData = it)
                    }
                    viewModel.putInDetailList(list)
                }
            }

            ManageType.ACTIVITY -> {
                viewModel.activityList.observe(viewLifecycleOwner) {
                    val list = mutableListOf<ItemData>()
                    val newList = it.filter {
                        it.status != 2
                    }
                    newList.forEach {
                        list += ItemData(ActivityData = it)
                    }
                    viewModel.putInDetailList(list)
                }
            }

            ManageType.CARE -> {
                viewModel.careList.observe(viewLifecycleOwner) {
                    val list = mutableListOf<ItemData>()
                    val newList = it.filter {
                        it.status != 2
                    }
                    newList.forEach {
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

        viewModel.isTheNewBie.observe(viewLifecycleOwner) {
            if (it) {
                binding.lavEmptyItems.visibility = View.VISIBLE
                binding.tvNewbieSlogan.visibility = View.VISIBLE
            } else {
                binding.lavEmptyItems.visibility = View.GONE
                binding.tvNewbieSlogan.visibility = View.GONE
            }
        }

        binding.rvManageItems.adapter = adapter
        binding.btAddItem.setOnClickListener {
            findNavController().navigate(
                NavigationDirections.actionGlobalItemEditFragment(manageType, user)
            )
        }
        return binding.root
    }
}
