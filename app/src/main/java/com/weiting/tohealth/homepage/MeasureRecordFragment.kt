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
import com.weiting.tohealth.R
import com.weiting.tohealth.data.MeasureLog
import com.weiting.tohealth.databinding.MeasureRecordFragmentBinding
import com.weiting.tohealth.factory.RecordViewModelFactory

class MeasureRecordFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = MeasureRecordFragmentBinding.inflate(inflater, container, false)
        val measureData = MeasureRecordFragmentArgs.fromBundle(requireArguments()).measureData
        val timeTag = MeasureRecordFragmentArgs.fromBundle(requireArguments()).timeTag
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

                    tvTitle.text = getString(R.string.heartBeatTitle)
                }
                1 -> {
                    tvDiastolic.visibility = View.GONE
                    tvSystolic.visibility = View.GONE
                    textView2.visibility = View.GONE
                    edtSystolic.visibility = View.GONE
                    edtDiastolic.visibility = View.GONE

                    tvTitle.text = getString(R.string.bloodSugarBeforeMeal)

                }
                2 -> {
                    tvDiastolic.visibility = View.GONE
                    tvSystolic.visibility = View.GONE
                    textView2.visibility = View.GONE
                    edtSystolic.visibility = View.GONE
                    edtDiastolic.visibility = View.GONE

                    tvTitle.text = getString(R.string.bloodSugarAfterMeal)
                }
                3 -> {
                    tvDiastolic.visibility = View.GONE
                    tvSystolic.visibility = View.GONE
                    textView2.visibility = View.GONE
                    edtSystolic.visibility = View.GONE
                    edtDiastolic.visibility = View.GONE

                    tvTitle.text = getString(R.string.spo2)
                }
                4 -> {
                    tvDiastolic.visibility = View.GONE
                    tvSystolic.visibility = View.GONE
                    textView2.visibility = View.GONE
                    edtSystolic.visibility = View.GONE
                    edtDiastolic.visibility = View.GONE

                    tvTitle.text = getString(R.string.bodyWeight)
                }
                5 -> {
                    tvDiastolic.visibility = View.GONE
                    tvSystolic.visibility = View.GONE
                    textView2.visibility = View.GONE
                    edtSystolic.visibility = View.GONE
                    edtDiastolic.visibility = View.GONE

                    tvTitle.text = getString(R.string.temperture)
                }
            }

            btEnterMeasure.setOnClickListener {
                when (measureData.type) {
                    0 -> {
                        viewModel.postMeasureLog(
                            itemId = measureData.id?:"",
                            measureLog = MeasureLog(
                                timeTag = timeTag,
                                result = 0,
                                createdTime = Timestamp.now(),
                                record = mapOf(
                                    "X" to binding.edtDiastolic.editableText.toString().toInt(),
                                    "Y" to binding.edtSystolic.editableText.toString().toInt(),
                                    "Z" to binding.editTextNumber.editableText.toString()
                                        .toInt()
                                )
                            ), measureData.type
                        )
                    }
                    else -> {
                        viewModel.postMeasureLog(
                            itemId = measureData.id?:"",
                            measureLog = MeasureLog(
                                timeTag = timeTag,
                                result = 0,
                                createdTime = Timestamp.now(),
                                record = mapOf(
                                    "X" to binding.editTextNumber.editableText.toString()
                                        .toInt(),
                                    "Y" to null,
                                    "Z" to null
                                )
                            ), measureData.type?:0
                        )
                    }
                }
                Toast.makeText(context, getString(R.string.logFinished), Toast.LENGTH_LONG).show()
                findNavController().navigate(NavigationDirections.actionGlobalHomeFragment())
            }

            btCancelMeasure.setOnClickListener {
                findNavController().navigate(NavigationDirections.actionGlobalHomeFragment())
            }
        }
        return binding.root
    }


}