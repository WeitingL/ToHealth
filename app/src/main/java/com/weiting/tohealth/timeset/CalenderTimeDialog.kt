package com.weiting.tohealth.timeset

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.Timestamp
import com.weiting.tohealth.NavigationDirections
import com.weiting.tohealth.databinding.DialogCalendertimeBinding
import com.weiting.tohealth.itemeditpage.ItemEditViewModel
import com.weiting.tohealth.toStringFromCalender
import com.weiting.tohealth.toStringFromMilliTime
import java.util.*

enum class EditTimeType {
    TIME, DATEANDTIME
}

class CalenderTimeDialog : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DialogCalendertimeBinding.inflate(inflater, container, false)
        val editTimeType = CalenderTimeDialogArgs.fromBundle(requireArguments()).timeEditType

        binding.btEnterTime.setOnClickListener {
            val time = binding.timePicker
            val date = binding.datePicker
            val milliTime = toStringFromCalender(date.year, date.month, date.dayOfMonth, time.hour, time.minute)

            if (editTimeType == EditTimeType.DATEANDTIME){
                setFragmentResult("GetTimeAndDate", bundleOf("TimeAndDate" to milliTime))
                findNavController().popBackStack()
            }else{
                setFragmentResult("GetTimeAndDate", bundleOf("TimeAndDate" to milliTime))
                findNavController().popBackStack()
            }
        }

        return binding.root
    }

//    1634740570383
}