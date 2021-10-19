package com.weiting.tohealth.homepage

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
import com.weiting.tohealth.databinding.FragmentHomeBinding
import com.weiting.tohealth.factory.HomeViewModelFactory
import com.weiting.tohealth.itemeditpage.ItemEditFragment

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        val homeAdapter = HomeAdapter(HomeAdapter.OnclickListener {
            when(it){
                is HomePageItem.NextTask ->{
                    findNavController().navigate(NavigationDirections.actionGlobalTodoListFragment())
                }

                is HomePageItem.AddNewItem -> {
                    findNavController().navigate(NavigationDirections.actionGlobalItemEditFragment())
                }

                else -> { }
            }
        })

        val factory = HomeViewModelFactory(PublicApplication.application.firebaseDataRepository)
        val viewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)

        viewModel.nextTaskList.observe(viewLifecycleOwner){
            homeAdapter.submitList(it)
        }

        binding.apply {
            rvHomeCardView.adapter = homeAdapter
        }

        return binding.root
    }

}