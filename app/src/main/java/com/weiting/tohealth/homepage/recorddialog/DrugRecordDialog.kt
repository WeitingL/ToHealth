package com.weiting.tohealth.homepage.recorddialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.weiting.tohealth.PublicApplication
import com.weiting.tohealth.data.ItemType
import com.weiting.tohealth.databinding.DialogDrugRecordBinding
import com.weiting.tohealth.factory.RecordViewModelFactory

class DrugRecordDialog: DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DialogDrugRecordBinding.inflate(inflater, container, false)
        val factory = RecordViewModelFactory(PublicApplication.application.firebaseDataRepository)
        val viewModel = ViewModelProvider(this, factory).get(RecordViewModel::class.java)
        val drug = DrugRecordDialogArgs.fromBundle(requireArguments()).drugData

        binding.appCompatButton.setOnClickListener {
            viewModel.postRecord(ItemType.DRUG, drug.id!!)
            findNavController().popBackStack()
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        val width = (resources.displayMetrics.widthPixels * 0.5).toInt()
        dialog!!.window?.setLayout(width * 2, width)

    }

}