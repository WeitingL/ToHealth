package com.weiting.tohealth.alertmessagepage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.weiting.tohealth.PublicApplication
import com.weiting.tohealth.R
import com.weiting.tohealth.databinding.AltermessageFragmentBinding
import com.weiting.tohealth.factory.NotificationRecordViewModelFactory

class AlertMessageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = AltermessageFragmentBinding.inflate(inflater, container, false)
        val memberList = AlertMessageFragmentArgs.fromBundle(requireArguments()).memberList.toList()

        val factory = NotificationRecordViewModelFactory(
            PublicApplication.application.firebaseDataRepository,
            memberList
        )
        val viewModel = ViewModelProvider(this, factory).get(AlertMessageViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.liveAlterMessageRecord.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.lavLoagindNotification.visibility = View.VISIBLE
                binding.lavLoagindNotification.setAnimation(R.raw.empty_box)
            } else {
                viewModel.transferToAlterMessageRecord(it)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.lavLoagindNotification.setAnimation(R.raw.loading)
                binding.lavLoagindNotification.visibility = View.VISIBLE
            } else {
                binding.lavLoagindNotification.visibility = View.GONE
            }
        }



        return binding.root
    }
}
