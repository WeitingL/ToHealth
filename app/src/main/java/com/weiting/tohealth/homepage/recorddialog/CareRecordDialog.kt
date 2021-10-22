package com.weiting.tohealth.homepage.recorddialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.weiting.tohealth.PublicApplication
import com.weiting.tohealth.R
import com.weiting.tohealth.data.ItemType
import com.weiting.tohealth.databinding.DialogCareRecordBinding
import com.weiting.tohealth.factory.RecordViewModelFactory

class CareRecordDialog : DialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DialogCareRecordBinding.inflate(inflater, container, false)
        val careData = CareRecordDialogArgs.fromBundle(requireArguments()).careData
        val factory = RecordViewModelFactory(PublicApplication.application.firebaseDataRepository)
        val viewModel = ViewModelProvider(this, factory).get(RecordViewModel::class.java)

        binding.apply {

            ibCry.setOnClickListener {
                ibCry.setBackgroundResource(R.drawable.row_block)
                ibGoingtody.setBackgroundResource(R.color.white)
                ibSoso.setBackgroundResource(R.color.white)
                ibSmile.setBackgroundResource(R.color.white)
                ibLittleSick.setBackgroundResource(R.color.white)
                ibSad.setBackgroundResource(R.color.white)
                ibSick.setBackgroundResource(R.color.white)
                ibHappy.setBackgroundResource(R.color.white)
                viewModel.getCareScore(5)
            }

            ibGoingtody.setOnClickListener {
                ibCry.setBackgroundResource(R.color.white)
                ibGoingtody.setBackgroundResource(R.drawable.row_block)
                ibSoso.setBackgroundResource(R.color.white)
                ibSmile.setBackgroundResource(R.color.white)
                ibLittleSick.setBackgroundResource(R.color.white)
                ibSad.setBackgroundResource(R.color.white)
                ibSick.setBackgroundResource(R.color.white)
                ibHappy.setBackgroundResource(R.color.white)
                viewModel.getCareScore(7)
            }

            ibSoso.setOnClickListener {
                ibCry.setBackgroundResource(R.color.white)
                ibGoingtody.setBackgroundResource(R.color.white)
                ibSoso.setBackgroundResource(R.drawable.row_block)
                ibSmile.setBackgroundResource(R.color.white)
                ibLittleSick.setBackgroundResource(R.color.white)
                ibSad.setBackgroundResource(R.color.white)
                ibSick.setBackgroundResource(R.color.white)
                ibHappy.setBackgroundResource(R.color.white)
                viewModel.getCareScore(2)
            }

            ibSmile.setOnClickListener {
                ibCry.setBackgroundResource(R.color.white)
                ibGoingtody.setBackgroundResource(R.color.white)
                ibSoso.setBackgroundResource(R.color.white)
                ibSmile.setBackgroundResource(R.drawable.row_block)
                ibLittleSick.setBackgroundResource(R.color.white)
                ibSad.setBackgroundResource(R.color.white)
                ibSick.setBackgroundResource(R.color.white)
                ibHappy.setBackgroundResource(R.color.white)
                viewModel.getCareScore(1)
            }

            ibLittleSick.setOnClickListener {
                ibCry.setBackgroundResource(R.color.white)
                ibGoingtody.setBackgroundResource(R.color.white)
                ibSoso.setBackgroundResource(R.color.white)
                ibSmile.setBackgroundResource(R.color.white)
                ibLittleSick.setBackgroundResource(R.drawable.row_block)
                ibSad.setBackgroundResource(R.color.white)
                ibSick.setBackgroundResource(R.color.white)
                ibHappy.setBackgroundResource(R.color.white)
                viewModel.getCareScore(4)
            }

            ibSad.setOnClickListener {
                ibCry.setBackgroundResource(R.color.white)
                ibGoingtody.setBackgroundResource(R.color.white)
                ibSoso.setBackgroundResource(R.color.white)
                ibSmile.setBackgroundResource(R.color.white)
                ibLittleSick.setBackgroundResource(R.color.white)
                ibSad.setBackgroundResource(R.drawable.row_block)
                ibSick.setBackgroundResource(R.color.white)
                ibHappy.setBackgroundResource(R.color.white)
                viewModel.getCareScore(3)
            }

            ibSick.setOnClickListener {
                ibCry.setBackgroundResource(R.color.white)
                ibGoingtody.setBackgroundResource(R.color.white)
                ibSoso.setBackgroundResource(R.color.white)
                ibSmile.setBackgroundResource(R.color.white)
                ibLittleSick.setBackgroundResource(R.color.white)
                ibSad.setBackgroundResource(R.color.white)
                ibSick.setBackgroundResource(R.drawable.row_block)
                ibHappy.setBackgroundResource(R.color.white)
                viewModel.getCareScore(6)
            }

            ibHappy.setOnClickListener {
                ibCry.setBackgroundResource(R.color.white)
                ibGoingtody.setBackgroundResource(R.color.white)
                ibSoso.setBackgroundResource(R.color.white)
                ibSmile.setBackgroundResource(R.color.white)
                ibLittleSick.setBackgroundResource(R.color.white)
                ibSad.setBackgroundResource(R.color.white)
                ibSick.setBackgroundResource(R.color.white)
                ibHappy.setBackgroundResource(R.drawable.row_block)
                viewModel.getCareScore(0)
            }

            btEnterCareLog.setOnClickListener {
                viewModel.getInfo(binding.editTextTextPersonName.editableText.toString())
                viewModel.postRecord(ItemType.CARE, careData.id!!)
                findNavController().popBackStack()
            }

        }

        return binding.root
    }

}