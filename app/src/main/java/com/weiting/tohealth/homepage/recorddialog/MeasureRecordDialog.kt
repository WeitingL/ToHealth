package com.weiting.tohealth.homepage.recorddialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.weiting.tohealth.PublicApplication
import com.weiting.tohealth.data.ItemType
import com.weiting.tohealth.databinding.DialogMeasureRecordBinding
import com.weiting.tohealth.factory.RecordViewModelFactory

class MeasureRecordDialog : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DialogMeasureRecordBinding.inflate(inflater, container, false)
        val measureData = MeasureRecordDialogArgs.fromBundle(requireArguments()).measureData
        val factory = RecordViewModelFactory(PublicApplication.application.firebaseDataRepository)
        val viewModel = ViewModelProvider(this, factory).get(RecordViewModel::class.java)

        binding.apply {
            when (measureData.type) {
                0 -> {
                    tvDiastolic.visibility = View.VISIBLE
                    tvSystolic.visibility = View.VISIBLE
                    textView5.visibility = View.VISIBLE
                    edtSystolic.visibility = View.VISIBLE
                    edtDiastolic.visibility = View.VISIBLE

                    tvTitle.text = "心搏"
                }
                1 -> {
                    tvDiastolic.visibility = View.GONE
                    tvSystolic.visibility = View.GONE
                    textView5.visibility = View.GONE
                    edtSystolic.visibility = View.GONE
                    edtDiastolic.visibility = View.GONE

                    tvTitle.text = "飯前血糖"

                }
                2 -> {
                    tvDiastolic.visibility = View.GONE
                    tvSystolic.visibility = View.GONE
                    textView5.visibility = View.GONE
                    edtSystolic.visibility = View.GONE
                    edtDiastolic.visibility = View.GONE

                    tvTitle.text = "飯後血糖"
                }
                3 -> {
                    tvDiastolic.visibility = View.GONE
                    tvSystolic.visibility = View.GONE
                    textView5.visibility = View.GONE
                    edtSystolic.visibility = View.GONE
                    edtDiastolic.visibility = View.GONE

                    tvTitle.text = "血氧(SpO2)"
                }
                4 -> {
                    tvDiastolic.visibility = View.GONE
                    tvSystolic.visibility = View.GONE
                    textView5.visibility = View.GONE
                    edtSystolic.visibility = View.GONE
                    edtDiastolic.visibility = View.GONE

                    tvTitle.text = "體重(Kg, 公斤)"
                }
                5 -> {
                    tvDiastolic.visibility = View.GONE
                    tvSystolic.visibility = View.GONE
                    textView5.visibility = View.GONE
                    edtSystolic.visibility = View.GONE
                    edtDiastolic.visibility = View.GONE

                    tvTitle.text = "體溫(C, 攝氏)"
                }
            }

            btEnterMeasure.setOnClickListener {
                when (measureData.type) {
                    0 -> {
                        viewModel.getMeasureData(
                            binding.edtDiastolic.editableText.toString().toInt(),
                            binding.edtSystolic.editableText.toString().toInt(),
                            binding.editTextNumber3.editableText.toString().toInt()
                        )
                    }
                    else -> {
                        viewModel.getMeasureData(
                            binding.editTextNumber3.editableText.toString().toInt(), null, null
                        )
                    }
                }
                viewModel.postRecord(ItemType.MEASURE, measureData.id!!)
            }
        }


        return binding.root
    }


    override fun onStart() {
        super.onStart()

        val width = (resources.displayMetrics.widthPixels * 0.8).toInt()
        dialog!!.window?.setLayout(width, width)

    }
}