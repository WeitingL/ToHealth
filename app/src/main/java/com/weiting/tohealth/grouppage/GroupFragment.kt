package com.weiting.tohealth.grouppage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.weiting.tohealth.NavigationDirections
import com.weiting.tohealth.databinding.FragmentGroupBinding

class GroupFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentGroupBinding.inflate(layoutInflater, container, false)
        val viewModel = ViewModelProvider(this).get(GroupViewModel::class.java)
        val adapter = GroupAdapter(GroupAdapter.OnclickListener {
            when(it){
                is GroupPageItem.MyGroups ->{
                    Toast.makeText(context, "WOW", Toast.LENGTH_LONG).show()
                }

                is GroupPageItem.AddGroups ->{
                    findNavController().navigate(NavigationDirections.actionGlobalAddNewGroupFragment())
                }
            }
        })

        viewModel.groupItemList.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }

        binding.rvGroupList.adapter = adapter
        return binding.root
    }

}