package com.weiting.tohealth.itemeditpage

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.weiting.tohealth.NavigationDirections
import com.weiting.tohealth.PublicApplication
import com.weiting.tohealth.R
import com.weiting.tohealth.data.*
import com.weiting.tohealth.databinding.ItemEditFragmentBinding
import com.weiting.tohealth.factory.ItemEditViewModelFactory
import com.weiting.tohealth.mymanagepage.ManageType
import com.weiting.tohealth.timeset.EditTimeType

class ItemEditFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = ItemEditFragmentBinding.inflate(inflater, container, false)
        val editType = ItemEditFragmentArgs.fromBundle(requireArguments()).editType
        val manageType = ItemEditFragmentArgs.fromBundle(requireArguments()).manageType
        val factory =
            ItemEditViewModelFactory(PublicApplication.application.firebaseDataRepository, editType)
        val viewModel = ViewModelProvider(this, factory).get(ItemEditViewModel::class.java)

        val position = when(manageType){
            ManageType.DRUG -> 0
            ManageType.MEASURE -> 1
            ManageType.ACTIVITY -> 2
            ManageType.CARE -> 3
        }

        binding.spItemType.setSelection(position)

        //Listen the spinner item selected
        binding.spItemType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                viewModel.getSelectedItemType(p2)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        binding.spEndDate.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                viewModel.getEndDateSelectedType(p2)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}

        }

        binding.spPeriod.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                viewModel.getCurrentPeriodType(p2)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}

        }

        binding.spSubtype.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                viewModel.getcurrentPeriodSubType(p2)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}

        }

        //Get editType spinner selected to change views
        viewModel.editItemType.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    ItemType.DRUG -> {
                        tvDrugname.visibility = View.VISIBLE
                        tilDrugName.visibility = View.VISIBLE
                        tvItemNameEdit.visibility = View.GONE
                        spItemName.visibility = View.GONE
                        tvFirstDate.visibility = View.VISIBLE
                        tvFirstDateTitle.visibility = View.VISIBLE
                        clUnit.visibility = View.VISIBLE
                        clEndDate.visibility = View.VISIBLE
                        clPeriod.visibility = View.VISIBLE
                        clStock.visibility = View.VISIBLE
                    }

                    ItemType.MEASURE -> {
                        tvDrugname.visibility = View.GONE
                        tilDrugName.visibility = View.GONE
                        tvItemNameEdit.visibility = View.VISIBLE
                        spItemName.visibility = View.VISIBLE
                        spItemName.adapter = ArrayAdapter.createFromResource(
                            PublicApplication.application.applicationContext,
                            R.array.Measure_Item,
                            android.R.layout.simple_spinner_dropdown_item
                        )
                        tvFirstDate.visibility = View.GONE
                        tvFirstDateTitle.visibility = View.GONE
                        clUnit.visibility = View.GONE
                        clEndDate.visibility = View.GONE
                        clPeriod.visibility = View.GONE
                        clStock.visibility = View.GONE
                    }

                    ItemType.ACTIVITY -> {
                        tvDrugname.visibility = View.GONE
                        tilDrugName.visibility = View.GONE
                        tvItemNameEdit.visibility = View.VISIBLE
                        spItemName.visibility = View.VISIBLE
                        spItemName.adapter = ArrayAdapter.createFromResource(
                            PublicApplication.application.applicationContext,
                            R.array.Activity_Item,
                            android.R.layout.simple_spinner_dropdown_item
                        )
                        tvFirstDate.visibility = View.VISIBLE
                        tvFirstDateTitle.visibility = View.VISIBLE

                        clUnit.visibility = View.GONE
                        clEndDate.visibility = View.VISIBLE
                        clPeriod.visibility = View.VISIBLE
                        clStock.visibility = View.GONE

                    }

                    ItemType.CARE -> {
                        tvDrugname.visibility = View.GONE
                        tilDrugName.visibility = View.GONE
                        tvItemNameEdit.visibility = View.VISIBLE
                        spItemName.visibility = View.VISIBLE
                        spItemName.adapter = ArrayAdapter.createFromResource(
                            PublicApplication.application.applicationContext,
                            R.array.Care_Item,
                            android.R.layout.simple_spinner_dropdown_item
                        )
                        tvFirstDate.visibility = View.VISIBLE
                        tvFirstDateTitle.visibility = View.VISIBLE

                        clUnit.visibility = View.GONE
                        clEndDate.visibility = View.VISIBLE
                        clPeriod.visibility = View.VISIBLE
                        clStock.visibility = View.GONE
                    }
                    else -> {
                    }
                }
            }
        }

        //Get endDate spinner selected to change views
        viewModel.endDateSelected.observe(viewLifecycleOwner) {

            binding.apply {
                when (it) {
                    0 -> {
                        tvDayEndDate.visibility = View.GONE
                        spDayEdit.visibility = View.GONE
                    }

                    1 -> {
                        tvDayEndDate.visibility = View.VISIBLE
                        spDayEdit.visibility = View.VISIBLE
                        spDayEdit.adapter = ArrayAdapter.createFromResource(
                            PublicApplication.application.applicationContext,
                            R.array.cycle_day,
                            android.R.layout.simple_spinner_dropdown_item
                        )
                    }
                }
            }
        }

        //Get periodType spinner selected to change views
        viewModel.currentPeriodType.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    0 -> {
                        tvOngoingUnit.visibility = View.VISIBLE
                        tvOngoingUnit.text = "次數"
                        spOngoingUnit.visibility = View.VISIBLE
                        spOngoingUnit.adapter = ArrayAdapter.createFromResource(
                            PublicApplication.application.applicationContext,
                            R.array.Number,
                            android.R.layout.simple_spinner_dropdown_item
                        )
                        tvSuspendDay.visibility = View.GONE
                        spSuspendDay.visibility = View.GONE
                        tvCycle.visibility = View.GONE
                        spCycle.visibility = View.GONE
                        tvSubtype.visibility = View.GONE
                        spSubtype.visibility = View.GONE
                        tvSubOngoningUnit.visibility = View.GONE
                        spSubOngoningUnit.visibility = View.GONE
                    }
                    1 -> {
                        tvOngoingUnit.visibility = View.VISIBLE
                        tvOngoingUnit.text = "小時"
                        spOngoingUnit.visibility = View.VISIBLE
                        spOngoingUnit.adapter = ArrayAdapter.createFromResource(
                            PublicApplication.application.applicationContext,
                            R.array.hour,
                            android.R.layout.simple_spinner_dropdown_item
                        )
                        tvSuspendDay.visibility = View.GONE
                        spSuspendDay.visibility = View.GONE
                        tvCycle.visibility = View.GONE
                        spCycle.visibility = View.GONE
                        tvSubtype.visibility = View.GONE
                        spSubtype.visibility = View.GONE
                        tvSubOngoningUnit.visibility = View.GONE
                        spSubOngoningUnit.visibility = View.GONE
                    }

                    2 -> {
                        tvOngoingUnit.visibility = View.VISIBLE
                        tvOngoingUnit.text = "日"
                        spOngoingUnit.visibility = View.VISIBLE
                        spOngoingUnit.adapter = ArrayAdapter.createFromResource(
                            PublicApplication.application.applicationContext,
                            R.array.day,
                            android.R.layout.simple_spinner_dropdown_item
                        )
                        tvSuspendDay.visibility = View.GONE
                        spSuspendDay.visibility = View.GONE
                        tvCycle.visibility = View.GONE
                        spCycle.visibility = View.GONE
                        tvSubtype.visibility = View.GONE
                        spSubtype.visibility = View.GONE
                        tvSubOngoningUnit.visibility = View.GONE
                        spSubOngoningUnit.visibility = View.GONE
                    }
                    3 -> {
                        tvOngoingUnit.visibility = View.VISIBLE
                        tvOngoingUnit.text = "星期"
                        spOngoingUnit.visibility = View.VISIBLE
                        spOngoingUnit.adapter = ArrayAdapter.createFromResource(
                            PublicApplication.application.applicationContext,
                            R.array.week,
                            android.R.layout.simple_spinner_dropdown_item
                        )
                        tvSuspendDay.visibility = View.GONE
                        spSuspendDay.visibility = View.GONE
                        tvCycle.visibility = View.GONE
                        spCycle.visibility = View.GONE
                        tvSubtype.visibility = View.GONE
                        spSubtype.visibility = View.GONE
                        tvSubOngoningUnit.visibility = View.GONE
                        spSubOngoningUnit.visibility = View.GONE
                    }
                    4 -> {
                        tvOngoingUnit.visibility = View.VISIBLE
                        tvOngoingUnit.visibility = View.VISIBLE
                        spOngoingUnit.adapter = ArrayAdapter.createFromResource(
                            PublicApplication.application.applicationContext,
                            R.array.cycle_day,
                            android.R.layout.simple_spinner_dropdown_item
                        )
                        tvSuspendDay.visibility = View.VISIBLE
                        spSuspendDay.visibility = View.VISIBLE
                        spSuspendDay.adapter = ArrayAdapter.createFromResource(
                            PublicApplication.application.applicationContext,
                            R.array.cycle_day,
                            android.R.layout.simple_spinner_dropdown_item
                        )

                        tvCycle.visibility = View.VISIBLE
                        tvCycle.text = "循環"
                        spCycle.visibility = View.VISIBLE
                        spCycle.adapter = ArrayAdapter.createFromResource(
                            PublicApplication.application.applicationContext,
                            R.array.Number,
                            android.R.layout.simple_spinner_dropdown_item
                        )

                        tvSubtype.visibility = View.VISIBLE
                        spSubtype.visibility = View.VISIBLE
                        spSubtype.adapter = ArrayAdapter.createFromResource(
                            PublicApplication.application.applicationContext,
                            R.array.Sub_Period,
                            android.R.layout.simple_spinner_dropdown_item
                        )
                        tvSubOngoningUnit.visibility = View.VISIBLE
                        spSubOngoningUnit.visibility = View.VISIBLE
                    }
                    5 -> {
                        tvOngoingUnit.visibility = View.GONE
                        spOngoingUnit.visibility = View.GONE
                        tvSuspendDay.visibility = View.GONE
                        spSuspendDay.visibility = View.GONE
                        tvCycle.visibility = View.GONE
                        spCycle.visibility = View.GONE
                        tvSubtype.visibility = View.GONE
                        spSubtype.visibility = View.GONE
                        tvSubOngoningUnit.visibility = View.GONE
                        spSubOngoningUnit.visibility = View.GONE
                    }
                }
            }
        }

        //Get PeriodSubType spinner selected to change views
        viewModel.currentPeriodSubType.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    0 -> {
                        spSubOngoningUnit.adapter = ArrayAdapter.createFromResource(
                            PublicApplication.application.applicationContext,
                            R.array.Number,
                            android.R.layout.simple_spinner_dropdown_item
                        )
                    }

                    1 -> {
                        spSubOngoningUnit.adapter = ArrayAdapter.createFromResource(
                            PublicApplication.application.applicationContext,
                            R.array.hour,
                            android.R.layout.simple_spinner_dropdown_item
                        )
                    }

                    2 -> {
                        spSubOngoningUnit.adapter = ArrayAdapter.createFromResource(
                            PublicApplication.application.applicationContext,
                            R.array.day,
                            android.R.layout.simple_spinner_dropdown_item
                        )
                    }

                    3 -> {
                        spSubOngoningUnit.adapter = ArrayAdapter.createFromResource(
                            PublicApplication.application.applicationContext,
                            R.array.week,
                            android.R.layout.simple_spinner_dropdown_item
                        )
                    }
                }
            }
        }

        //Get timeSet spinner selected to change views
        viewModel.timeSet.observe(viewLifecycleOwner) {
            binding.tvTimeSet.text = it
        }

        viewModel.dateSet.observe(viewLifecycleOwner) {
            binding.tvFirstDate.text = it
        }

        setFragmentResultListener("GetTime") { requestKey, bundle ->
            viewModel.getTimeSet(bundle.getLong("Time"))
        }

        setFragmentResultListener("GetDate") { requestKey, bundle ->
            viewModel.getDateSet(bundle.getLong("Date"))
        }

        binding.tvTimeSet.setOnClickListener {
            findNavController().navigate(
                NavigationDirections.actionGlobalCalenderTimeDialog(
                    EditTimeType.TIME
                )
            )
        }

        binding.tvFirstDate.setOnClickListener {
            findNavController().navigate(
                NavigationDirections.actionGlobalCalenderTimeDialog(
                    EditTimeType.DATE
                )
            )
        }

        binding.btEnterItem.setOnClickListener {
            when (checkInput(binding)) {
                true -> {
                    viewModel.postData(binding)
                    findNavController().popBackStack()
                }
                false -> Toast.makeText(context, "有東西還沒填喔!", Toast.LENGTH_LONG).show()
            }
        }
        return binding.root
    }

    private fun checkInput(binding: ItemEditFragmentBinding): Boolean {

       return when(binding.spItemType.selectedItemPosition){
            0 ->{
                when {
                    binding.tilDrugName.editText?.text.isNullOrEmpty() -> {
                        false
                    }
                    binding.tvFirstDate.text.isNullOrEmpty() -> {
                        false
                    }
                    binding.tvTimeSet.text.isNullOrEmpty() -> {
                        false
                    }
                    binding.etvStock.text.isNullOrEmpty() -> {
                        false
                    }
                    else -> {
                        true
                    }
                }
            }

            1 -> {
                when {
                    binding.tvTimeSet.text.isNullOrEmpty() -> {
                        false
                    }
                    else -> {
                        true
                    }
                }
            }

            2 -> {
                when {
                    binding.tvFirstDate.text.isNullOrEmpty() -> {
                        false
                    }
                    binding.tvTimeSet.text.isNullOrEmpty() -> {
                        false
                    }
                    else -> {
                        true
                    }
                }
            }

            3 -> {
                when {
                    binding.tvFirstDate.text.isNullOrEmpty() -> {
                        false
                    }
                    binding.tvTimeSet.text.isNullOrEmpty() -> {
                        false
                    }
                    else -> {
                        true
                    }
                }
            }
           else -> true
       }



    }
}