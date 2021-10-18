package com.weiting.tohealth.homepage.carddetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.weiting.tohealth.databinding.TodolistFragmentBinding

class TodoListFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = TodolistFragmentBinding.inflate(inflater, container, false)


        return binding.root
    }

}