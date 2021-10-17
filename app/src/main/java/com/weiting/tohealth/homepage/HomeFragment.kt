package com.weiting.tohealth.homepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.weiting.tohealth.R
import com.weiting.tohealth.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        val homeAdapter = HomeAdapter()
        val viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        viewModel.nextTaskList.observe(viewLifecycleOwner){
            homeAdapter.submitList(it)
        }

        binding.apply {
            rvHomeCardView.adapter = homeAdapter
        }

        return binding.root
    }

}