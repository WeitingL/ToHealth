package com.weiting.tohealth.statisticpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.weiting.tohealth.databinding.StatisticDetailFagmentBinding

class StatisticDetailFragment(type: StatisticType) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = StatisticDetailFagmentBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this).get(StatisticDetailViewModel::class.java)
        val adapter = StatisticDetailAdapter()

        viewModel.logList.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }

        binding.rvStatisticDataList.adapter = adapter
        return binding.root
    }

}