package com.weiting.tohealth.homepage.carddetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.weiting.tohealth.databinding.TodolistFragmentBinding
import com.weiting.tohealth.homepage.TodayItemAdapter

class TodoListFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = TodolistFragmentBinding.inflate(inflater, container, false)
        val adapter = TodayItemAdapter()
        val arg = TodoListFragmentArgs.fromBundle(requireArguments()).dataList

//        adapter.submitList()


        return binding.root
    }

}