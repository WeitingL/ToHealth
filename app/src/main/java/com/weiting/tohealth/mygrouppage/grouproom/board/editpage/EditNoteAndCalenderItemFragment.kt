package com.weiting.tohealth.mygrouppage.grouproom.board.editpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.Timestamp
import com.weiting.tohealth.NavigationDirections
import com.weiting.tohealth.PublicApplication
import com.weiting.tohealth.R
import com.weiting.tohealth.data.Reminder
import com.weiting.tohealth.data.Group
import com.weiting.tohealth.data.Note
import com.weiting.tohealth.data.UserManager
import com.weiting.tohealth.databinding.EditNoteandcalenderitemFragmentBinding
import com.weiting.tohealth.factory.EditNoteAndCalenderItemViewModelFactory
import com.weiting.tohealth.mygrouppage.grouproom.GROUP
import com.weiting.tohealth.timeset.EditTimeType
import com.weiting.tohealth.timeset.GET_TIME_AND_DAY
import com.weiting.tohealth.timeset.TIME_AND_DAY
import java.util.*

class EditNoteAndCalenderItemFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = EditNoteandcalenderitemFragmentBinding.inflate(inflater, container, false)
        val group: Group = arguments?.get(GROUP) as Group
        val factory = EditNoteAndCalenderItemViewModelFactory(
            PublicApplication.application.firebaseDataRepository,
            group
        )
        val viewModel =
            ViewModelProvider(this, factory).get(EditNoteAndCalenderItemViewModel::class.java)

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
                    EditTimeType.DATE_AND_TIME
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

                    else -> {
                    }
                }
            }
        }

        setFragmentResultListener(GET_TIME_AND_DAY) { _, bundle ->
            viewModel.getTimeSet(bundle.getLong(TIME_AND_DAY))
        }

        binding.button.setOnClickListener {

            when (viewModel.editBoardType.value) {
                EditBoardType.NOTE -> {
                    if (binding.etvTitle.text.isEmpty() || binding.etvContent.text.isEmpty()) {
                        Toast.makeText(context, getString(R.string.SomethingEmpty), Toast.LENGTH_LONG).show()
                    } else {
                        viewModel.postNote(
                            Note(
                                title = binding.etvTitle.text.toString(),
                                content = binding.etvContent.text.toString(),
                                editor = UserManager.UserInfo.id,
                                footer = binding.spFooter.selectedItemPosition,
                                createdTime = Timestamp.now()
                            )
                        )
                        findNavController().popBackStack()
                    }
                }

                EditBoardType.REMINDER -> {
                    if (binding.edvReminderTitle.text.isEmpty() || binding.tvEditDate.text.isEmpty()) {
                        Toast.makeText(context, getString(R.string.SomethingEmpty), Toast.LENGTH_LONG).show()
                    } else {
                        viewModel.postReminder(
                            Reminder(
                                editor = UserManager.UserInfo.id,
                                content = binding.edvReminderTitle.text.toString(),
                                date = Timestamp(Date(viewModel.longTime.value?:0L)),
                                createdTime = Timestamp.now(),
                                result = 0
                            )
                        )
                        findNavController().popBackStack()
                    }
                }
            }
        }

        return binding.root
    }
}
