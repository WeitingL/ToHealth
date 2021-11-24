package com.weiting.tohealth.timeset

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.weiting.tohealth.databinding.DialogCalendertimeBinding
import com.weiting.tohealth.util.Util.toTimeInMilliFromPicker

enum class EditTimeType {
    TIME, DATE, DATE_AND_TIME
}

const val GET_TIME_AND_DAY = "GetTimeAndDate"
const val TIME_AND_DAY = "TimeAndDate"
const val GET_TIME = "GetTime"
const val GET_DATE = "GetDate"
const val TIME = "Time"
const val DATE = "Date"

class CalenderTimeDialog : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DialogCalendertimeBinding.inflate(inflater, container, false)
        binding.timePicker.setIs24HourView(true)
        val editTimeType = CalenderTimeDialogArgs.fromBundle(requireArguments()).timeEditType

        // Control the picker needed.
        binding.apply {
            when (editTimeType) {
                EditTimeType.DATE -> {
                    clDatePicker.visibility = View.VISIBLE
                    clTimePicker.visibility = View.GONE
                }

                EditTimeType.TIME -> {
                    clDatePicker.visibility = View.GONE
                    clTimePicker.visibility = View.VISIBLE
                }

                EditTimeType.DATE_AND_TIME -> {
                    clDatePicker.visibility = View.VISIBLE
                    clTimePicker.visibility = View.VISIBLE
                }
            }
        }

        binding.btEnterTime.setOnClickListener {
            val time = binding.timePicker
            val date = binding.datePicker
            val milliTime = toTimeInMilliFromPicker(
                date.year,
                date.month,
                date.dayOfMonth,
                time.hour,
                time.minute
            )

            /*
            Get all Picker value, but need to pick only time or date up with other function in Util.
             */
            when (editTimeType) {
                EditTimeType.DATE -> {
                    setFragmentResult(GET_TIME, bundleOf(DATE to milliTime))
                }

                EditTimeType.TIME -> {
                    setFragmentResult(GET_TIME, bundleOf(TIME to milliTime))
                }

                EditTimeType.DATE_AND_TIME -> {
                    setFragmentResult(GET_TIME_AND_DAY, bundleOf(TIME_AND_DAY to milliTime))
                }
            }

            findNavController().popBackStack()
        }

        return binding.root
    }
}
