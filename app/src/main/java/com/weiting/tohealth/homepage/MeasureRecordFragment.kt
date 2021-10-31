package com.weiting.tohealth.homepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.Timestamp
import com.weiting.tohealth.NavigationDirections
import com.weiting.tohealth.PublicApplication
import com.weiting.tohealth.data.ItemData
import com.weiting.tohealth.data.ItemLogData
import com.weiting.tohealth.data.ItemType
import com.weiting.tohealth.data.MeasureLog
import com.weiting.tohealth.databinding.MeasureRecordFragmentBinding
import com.weiting.tohealth.factory.HomeViewModelFactory
import com.weiting.tohealth.factory.RecordViewModelFactory

class MeasureRecordFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = MeasureRecordFragmentBinding.inflate(inflater, container, false)
        val measureData = MeasureRecordFragmentArgs.fromBundle(requireArguments()).measureData
        val itemPosition = MeasureRecordFragmentArgs.fromBundle(requireArguments()).itemPosition
        val factory = RecordViewModelFactory(PublicApplication.application.firebaseDataRepository)
        val viewModel = ViewModelProvider(this, factory).get(RecordViewModel::class.java)

        binding.apply {
            when (measureData.type) {
                0 -> {
                    tvDiastolic.visibility = View.VISIBLE
                    tvSystolic.visibility = View.VISIBLE
                    textView2.visibility = View.VISIBLE
                    edtSystolic.visibility = View.VISIBLE
                    edtDiastolic.visibility = View.VISIBLE

                    tvTitle.text = "心搏"
                }
                1 -> {
                    tvDiastolic.visibility = View.GONE
                    tvSystolic.visibility = View.GONE
                    textView2.visibility = View.GONE
                    edtSystolic.visibility = View.GONE
                    edtDiastolic.visibility = View.GONE

                    tvTitle.text = "飯前血糖"

                }
                2 -> {
                    tvDiastolic.visibility = View.GONE
                    tvSystolic.visibility = View.GONE
                    textView2.visibility = View.GONE
                    edtSystolic.visibility = View.GONE
                    edtDiastolic.visibility = View.GONE

                    tvTitle.text = "飯後血糖"
                }
                3 -> {
                    tvDiastolic.visibility = View.GONE
                    tvSystolic.visibility = View.GONE
                    textView2.visibility = View.GONE
                    edtSystolic.visibility = View.GONE
                    edtDiastolic.visibility = View.GONE

                    tvTitle.text = "血氧(SpO2)"
                }
                4 -> {
                    tvDiastolic.visibility = View.GONE
                    tvSystolic.visibility = View.GONE
                    textView2.visibility = View.GONE
                    edtSystolic.visibility = View.GONE
                    edtDiastolic.visibility = View.GONE

                    tvTitle.text = "體重(Kg, 公斤)"
                }
                5 -> {
                    tvDiastolic.visibility = View.GONE
                    tvSystolic.visibility = View.GONE
                    textView2.visibility = View.GONE
                    edtSystolic.visibility = View.GONE
                    edtDiastolic.visibility = View.GONE

                    tvTitle.text = "體溫(C, 攝氏)"
                }
            }

            btEnterMeasure.setOnClickListener {
                when (measureData.type) {
                    0 -> {
                        viewModel.postMeasureLog(
                            itemId = measureData.id!!,
                            measureLog = MeasureLog(
                                result = 0,
                                createTime = Timestamp.now(),
                                record = mapOf(
                                    "X" to binding.edtDiastolic.editableText.toString().toInt(),
                                    "Y" to binding.edtSystolic.editableText.toString().toInt(),
                                    "Z" to binding.editTextNumber.editableText.toString()
                                        .toInt()
                                )
                            )
                        )
                    }
                    else -> {
                        viewModel.postMeasureLog(
                            itemId = measureData.id!!,
                            measureLog = MeasureLog(
                                result = 0,
                                createTime = Timestamp.now(),
                                record = mapOf(
                                    "X" to binding.editTextNumber.editableText.toString()
                                        .toInt(),
                                    "Y" to null,
                                    "Z" to null
                                )
                            )
                        )
                    }
                }
                Toast.makeText(context, "完成登錄!", Toast.LENGTH_LONG).show()
                findNavController().navigate(NavigationDirections.actionGlobalHomeFragment())
            }

            btCancelMeasure.setOnClickListener {
                findNavController().navigate(NavigationDirections.actionGlobalHomeFragment())
            }
        }
        return binding.root
    }


}