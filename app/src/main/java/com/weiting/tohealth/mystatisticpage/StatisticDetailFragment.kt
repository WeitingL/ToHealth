package com.weiting.tohealth.mystatisticpage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.weiting.tohealth.PublicApplication
import com.weiting.tohealth.data.User
import com.weiting.tohealth.databinding.MystatisticItemFagmentBinding
import com.weiting.tohealth.factory.StatisticDetailViewModelFactory

class StatisticDetailFragment() : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = MystatisticItemFagmentBinding.inflate(inflater, container, false)

        //Get the type from MyStatisticAdapter.
        val statisticType = arguments?.get("type") as StatisticType
        val user = arguments?.get("user") as User
        val factory =
            StatisticDetailViewModelFactory(
                PublicApplication.application.firebaseDataRepository,
                user.id!!,
                statisticType
            )
        val viewModel = ViewModelProvider(this, factory).get(StatisticDetailViewModel::class.java)
        val adapter = StatisticDetailAdapter()

        viewModel.logList.observe(viewLifecycleOwner) {
            if (it.size > 1){
                adapter.submitList(it)
                binding.lavEmptyList.visibility = View.GONE
                binding.tvEmptyList.visibility = View.GONE
            }
        }

        binding.rvStatisticDataList.adapter = adapter
        return binding.root
    }

}