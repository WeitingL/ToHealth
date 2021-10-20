package com.weiting.tohealth.itemeditpage

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.weiting.tohealth.NavigationDirections
import com.weiting.tohealth.PublicApplication
import com.weiting.tohealth.R
import com.weiting.tohealth.data.ItemType
import com.weiting.tohealth.databinding.ItemEditFragmentBinding
import com.weiting.tohealth.factory.ItemEditViewModelFactory
import com.weiting.tohealth.timeset.EditTimeType

class ItemEditFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = ItemEditFragmentBinding.inflate(inflater, container, false)
        val editType = ItemEditFragmentArgs.fromBundle(requireArguments()).editType
        val factory = ItemEditViewModelFactory(PublicApplication.application.firebaseDataRepository, editType)
        val viewModel = ViewModelProvider(this, factory).get(ItemEditViewModel::class.java)

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

        viewModel.editItemType.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    ItemType.DRUG -> {
                        tvDrugname.visibility = View.VISIBLE
                        tilDrugName.visibility = View.VISIBLE
                        tvItemNameEdit.visibility = View.GONE
                        spItemName.visibility = View.GONE

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

        viewModel.endDateSelected.observe(viewLifecycleOwner){

            binding.apply {
                when(it){
                    0 -> {
                        tvDayEndDate.visibility = View.GONE
                        spDayEdit.visibility = View.GONE
                    }

                    1 ->{
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

        viewModel.currentPeriodType.observe(viewLifecycleOwner){
            binding.apply {
                when(it){
                    0 ->{
                        tvOngoingDay.visibility = View.GONE
                        spOngoingDay.visibility = View.GONE
                        tvSuspendDay.visibility = View.GONE
                        spSuspendDay.visibility = View.GONE
                        tvCycle.visibility = View.VISIBLE
                        tvCycle.text = "次數"
                        spCycle.visibility = View.VISIBLE
                        spCycle.adapter = ArrayAdapter.createFromResource(
                            PublicApplication.application.applicationContext,
                            R.array.Number,
                            android.R.layout.simple_spinner_dropdown_item
                        )
                    }
                    1 ->{
                        tvOngoingDay.visibility = View.GONE
                        spOngoingDay.visibility = View.GONE
                        tvSuspendDay.visibility = View.GONE
                        spSuspendDay.visibility = View.GONE
                        tvCycle.visibility = View.VISIBLE
                        tvCycle.text = "小時"
                        spCycle.visibility = View.VISIBLE
                        spCycle.adapter = ArrayAdapter.createFromResource(
                            PublicApplication.application.applicationContext,
                            R.array.hour,
                            android.R.layout.simple_spinner_dropdown_item
                        )
                    }
                    2 ->{
                        tvOngoingDay.visibility = View.GONE
                        spOngoingDay.visibility = View.GONE
                        tvSuspendDay.visibility = View.GONE
                        spSuspendDay.visibility = View.GONE
                        tvCycle.visibility = View.VISIBLE
                        tvCycle.text = "日"
                        spCycle.visibility = View.VISIBLE
                        spCycle.adapter = ArrayAdapter.createFromResource(
                            PublicApplication.application.applicationContext,
                            R.array.day,
                            android.R.layout.simple_spinner_dropdown_item
                        )
                    }
                    3 ->{
                        tvOngoingDay.visibility = View.GONE
                        spOngoingDay.visibility = View.GONE
                        tvSuspendDay.visibility = View.GONE
                        spSuspendDay.visibility = View.GONE
                        tvCycle.visibility = View.VISIBLE
                        tvCycle.text = "星期"
                        spCycle.visibility = View.VISIBLE
                        spCycle.adapter = ArrayAdapter.createFromResource(
                            PublicApplication.application.applicationContext,
                            R.array.week,
                            android.R.layout.simple_spinner_dropdown_item
                        )
                    }
                    4 ->{
                        tvOngoingDay.visibility = View.VISIBLE
                        spOngoingDay.visibility = View.VISIBLE
                        spOngoingDay.adapter = ArrayAdapter.createFromResource(
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
                    }
                    5 ->{
                        tvOngoingDay.visibility = View.GONE
                        spOngoingDay.visibility = View.GONE
                        tvSuspendDay.visibility = View.GONE
                        spSuspendDay.visibility = View.GONE
                        tvCycle.visibility = View.GONE
                        spCycle.visibility = View.GONE
                    }
                }
            }
        }

        viewModel.timeSet.observe(viewLifecycleOwner){
            binding.tvTimeSet.text = it
        }

        setFragmentResultListener("GetTimeAndDate"){ requestKey, bundle ->
            viewModel.getTimeSet(bundle.getLong("TimeAndDate"))
        }

        binding.tvTimeSet.setOnClickListener {
            findNavController().navigate(NavigationDirections.actionGlobalCalenderTimeDialog(EditTimeType.TIME))
        }

        return binding.root
    }
}