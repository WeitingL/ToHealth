package com.weiting.tohealth.mystatisticpage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.weiting.tohealth.databinding.MystatisticItemFagmentBinding

class StatisticDetailFragment() : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = MystatisticItemFagmentBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this).get(StatisticDetailViewModel::class.java)
        val adapter = StatisticDetailAdapter()

        //Get the type from MyStatisticAdapter.
        val statisticType = arguments?.get("type")

        Log.i("What?", "$statisticType")
        viewModel.logList.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }

        binding.rvStatisticDataList.adapter = adapter
        return binding.root
    }

}