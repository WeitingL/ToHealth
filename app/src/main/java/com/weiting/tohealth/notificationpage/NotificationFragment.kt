package com.weiting.tohealth.notificationpage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.weiting.tohealth.PublicApplication
import com.weiting.tohealth.databinding.NotificationFragmentBinding
import com.weiting.tohealth.factory.NotificationRecordViewModelFactory

class NotificationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = NotificationFragmentBinding.inflate(inflater, container, false)
        val memberList = NotificationFragmentArgs.fromBundle(requireArguments()).memberList.toList()
        val factory = NotificationRecordViewModelFactory(
            PublicApplication.application.firebaseDataRepository,
            memberList
        )
        val viewModel = ViewModelProvider(this, factory).get(NotificationViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.notificationList.observe(viewLifecycleOwner){
            viewModel.transferToNotificationRecord(it)
        }


        return binding.root
    }


}