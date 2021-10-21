package com.weiting.tohealth.mygrouppage.grouproom.board.editpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.weiting.tohealth.NavigationDirections
import com.weiting.tohealth.databinding.EditNoteandcalenderitemFragmentBinding
import com.weiting.tohealth.timeset.EditTimeType

class EditNoteAndCalenderItemFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = EditNoteandcalenderitemFragmentBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this).get(EditNoteAndCalenderItemViewModel::class.java)

        binding.spEditNoteOrNot.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    viewModel.getEditBoardType(p2)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }

        binding.tvEditDate.setOnClickListener {
            findNavController().navigate(
                NavigationDirections.actionGlobalCalenderTimeDialog(
                    EditTimeType.DATEANDTIME
                )
            )
        }

        viewModel.timeSet.observe(viewLifecycleOwner) {
            binding.tvEditDate.text = it
        }

        viewModel.editBoardType.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    EditBoardType.NOTE -> {
                        clCalenderItem.visibility = View.GONE
                        clSticky.visibility = View.VISIBLE
                    }

                    EditBoardType.REMINDER -> {
                        clCalenderItem.visibility = View.VISIBLE
                        clSticky.visibility = View.GONE
                    }

                    else -> { }
                }
            }
        }

        setFragmentResultListener("GetTimeAndDate") { requestKey, bundle ->
            viewModel.getTimeSet(bundle.getLong("TimeAndDate"))
        }


        return binding.root
    }

}