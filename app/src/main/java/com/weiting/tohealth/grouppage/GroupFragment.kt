package com.weiting.tohealth.grouppage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.weiting.tohealth.R
import com.weiting.tohealth.databinding.FragmentGroupBinding

class GroupFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentGroupBinding.inflate(layoutInflater, container, false)
        val viewModel = ViewModelProvider(this).get(GroupViewModel::class.java)



        return binding.root
    }

}