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
import com.weiting.tohealth.R
import com.weiting.tohealth.data.ItemData
import com.weiting.tohealth.data.ItemType
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
        val itemType = arguments?.get(ITEM_TYPE) as ItemType
        val user = arguments?.get(USER) as User
        val private = arguments?.get(PRIVATE) as Int
        val factory = ManageDetailViewModelFactory(
            PublicApplication.application.firebaseDataRepository,
            user
        )
        val viewModel = ViewModelProvider(this, factory).get(ManageDetailViewModel::class.java)

        if (private == 2) {
            binding.btAddItem.visibility = View.GONE
        }

        val adapter = ManageDetailAdapter(
            ManageDetailAdapter.OnclickListener { itemData ->
                when (private == 2) {
                    true -> Toast.makeText(context, context?.getText(R.string.userDenyEdit), Toast.LENGTH_LONG).show()
                    false -> {
                        findNavController().navigate(
                            NavigationDirections.actionGlobalItemUpdateFragment(
                                itemData = itemData,
                                userInfo = user
                            )
                        )
                    }
                }
            }
        )

        when (itemType) {
            ItemType.DRUG -> {
                viewModel.drugList.observe(viewLifecycleOwner) {
                    val list = mutableListOf<ItemData>()
                    val newList = it.filter {
                        it.status != 2
                    }

                    newList.forEach {
                        list += ItemData(drugData = it)
                    }
                    viewModel.putInDetailList(list)
                }
            }

            ItemType.MEASURE -> {
                viewModel.measureList.observe(viewLifecycleOwner) {
                    val list = mutableListOf<ItemData>()
                    val newList = it.filter {
                        it.status != 2
                    }
                    newList.forEach {
                        list += ItemData(measureData = it)
                    }
                    viewModel.putInDetailList(list)
                }
            }

            ItemType.EVENT -> {
                viewModel.activityList.observe(viewLifecycleOwner) {
                    val list = mutableListOf<ItemData>()
                    val newList = it.filter {
                        it.status != 2
                    }
                    newList.forEach {
                        list += ItemData(eventData = it)
                    }
                    viewModel.putInDetailList(list)
                }
            }

            ItemType.CARE -> {
                viewModel.careList.observe(viewLifecycleOwner) {
                    val list = mutableListOf<ItemData>()
                    val newList = it.filter {
                        it.status != 2
                    }
                    newList.forEach {
                        list += ItemData(careData = it)
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
                NavigationDirections.actionGlobalItemEditFragment(itemType, user)
            )
        }
        return binding.root
    }
}
