package com.weiting.tohealth.homepage.fastadd

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.Timestamp
import com.weiting.tohealth.NavigationDirections
import com.weiting.tohealth.PublicApplication
import com.weiting.tohealth.data.*
import com.weiting.tohealth.databinding.FastAddFragmentBinding
import com.weiting.tohealth.factory.FastAddViewModelFactory
import com.weiting.tohealth.util.Util.getTimeStampToTimeInt

class FastAddFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FastAddFragmentBinding.inflate(inflater, container, false)
        val factory = FastAddViewModelFactory(PublicApplication.application.firebaseDataRepository)
        val viewModel = ViewModelProvider(this, factory).get(FastAddViewModel::class.java)

        val adapter = FastAddAdapter(
            FastAddAdapter.OnclickListener {
                when (it.itemType) {
                    ItemType.DRUG -> {
                        viewModel.postDrugLog(
                            it.drugData?.id?:"",
                            DrugLog(
                                timeTag = getTimeStampToTimeInt(Timestamp.now()),
                                result = 2,
                                createdTime = Timestamp.now()
                            ),
                            it.drugData?: Drug()
                        )
                        Toast.makeText(context, "已登錄紀錄", Toast.LENGTH_LONG).show()
                        findNavController().navigate(
                            NavigationDirections.actionGlobalHomeFragment()
                        )
                    }
                    ItemType.EVENT -> {
                        viewModel.postActivity(
                            it.eventData?.id?:"",
                            EventLog(
                                timeTag = getTimeStampToTimeInt(
                                    Timestamp.now()
                                ),
                                result = 2,
                                createdTime = Timestamp.now()
                            )
                        )
                        Toast.makeText(context, "已登錄紀錄", Toast.LENGTH_LONG).show()
                        findNavController().navigate(
                            NavigationDirections.actionGlobalHomeFragment()
                        )
                    }
                    ItemType.MEASURE -> {
                        findNavController().navigate(
                            NavigationDirections.actionGlobalMeasureRecordFragment(
                                it.measureData?: Measure(),
                                9999
                            )
                        )
                    }
                    else -> throw Exception("Something Wrong!")
                }
            }
        )

        viewModel.itemDataMediator.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()){
                binding.lavLoading.visibility = View.GONE
                adapter.submitList(it)
            }
        }

        binding.rvFastAdd.adapter = adapter
        return binding.root
    }
}
