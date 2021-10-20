package com.weiting.tohealth.timeset

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.weiting.tohealth.NavigationDirections
import com.weiting.tohealth.databinding.DialogCalendertimeBinding
import com.weiting.tohealth.itemeditpage.ItemEditViewModel

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

        binding.apply {

            clDatePicker.visibility = if (editTimeType == EditTimeType.DATEANDTIME) {
                View.VISIBLE
            } else {
                View.GONE
            }

        }

        binding.btEnterTime.setOnClickListener {
            if (editTimeType == EditTimeType.DATEANDTIME){
                setFragmentResult("GetTimeAndDate", bundleOf("TimeAndDate" to binding.timePicker.hour.toString()))
                findNavController().popBackStack()
            }else{
                setFragmentResult("GetTimeAndDate", bundleOf("TimeAndDate" to binding.timePicker.hour.toString()))
                findNavController().popBackStack()
            }
        }

        return binding.root
    }
}