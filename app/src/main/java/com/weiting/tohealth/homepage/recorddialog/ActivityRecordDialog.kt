package com.weiting.tohealth.homepage.recorddialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.weiting.tohealth.PublicApplication
import com.weiting.tohealth.data.ItemType
import com.weiting.tohealth.databinding.DialogActivityRecordBinding
import com.weiting.tohealth.factory.RecordViewModelFactory

class ActivityRecordDialog: DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DialogActivityRecordBinding.inflate(inflater, container, false)
        val factory = RecordViewModelFactory(PublicApplication.application.firebaseDataRepository)
        val activityData = ActivityRecordDialogArgs.fromBundle(requireArguments()).activityData
        val viewModel = ViewModelProvider(this, factory).get(RecordViewModel::class.java)

        binding.btFinished.setOnClickListener {
            viewModel.postRecord(ItemType.ACTIVITY, activityData.id!!)
        }


        return binding.root
    }


    override fun onStart() {
        super.onStart()

        val width = (resources.displayMetrics.widthPixels * 0.5).toInt()
        dialog!!.window?.setLayout(width * 2, width)

    }
}