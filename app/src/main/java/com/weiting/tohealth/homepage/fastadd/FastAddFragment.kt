package com.weiting.tohealth.homepage.fastadd

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.Timestamp
import com.weiting.tohealth.NavigationDirections
import com.weiting.tohealth.PublicApplication
import com.weiting.tohealth.data.ActivityLog
import com.weiting.tohealth.data.DrugLog
import com.weiting.tohealth.data.MeasureLog
import com.weiting.tohealth.databinding.FastAddFragmentBinding
import com.weiting.tohealth.factory.FastAddViewModelFactory
import com.weiting.tohealth.homepage.ItemDataType

class FastAddFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FastAddFragmentBinding.inflate(inflater, container, false)
        val factory = FastAddViewModelFactory(PublicApplication.application.firebaseDataRepository)
        val viewModel = ViewModelProvider(this, factory).get(FastAddViewModel::class.java)

        val adapter = FastAddAdapter(FastAddAdapter.OnclickListener {
            when (it) {
                is FastAddItem.DrugItem -> {
                    viewModel.postDrugLog(it.drug.id!!, DrugLog(
                        timeTag = 9999,
                        result = 2,
                        createTime = Timestamp.now()
                    ), it.drug)
                    findNavController().navigate(NavigationDirections.actionGlobalHomeFragment())
                }
                is FastAddItem.ActivityItem -> {
                    viewModel.postActivity(it.activity.id!!, ActivityLog(
                        timeTag = 9999,
                        result = 2,
                        createTime = Timestamp.now()
                    ))
                    findNavController().navigate(NavigationDirections.actionGlobalHomeFragment())
                }
                is FastAddItem.MeasureItem -> {
                    findNavController().navigate(NavigationDirections.actionGlobalMeasureRecordFragment(it.measure, 9999))
                }

            }
        })

        viewModel.itemDataMediator.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.rvFastAdd.adapter = adapter
        return binding.root
    }
}