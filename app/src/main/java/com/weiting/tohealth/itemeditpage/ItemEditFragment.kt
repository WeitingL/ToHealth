package com.weiting.tohealth.itemeditpage

import android.content.Context
import android.os.Bundle
import android.util.Log
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
        val manageType = ItemEditFragmentArgs.fromBundle(requireArguments()).manageType
        val factory = ItemEditViewModelFactory(PublicApplication.application.firebaseDataRepository)
        val viewModel = ViewModelProvider(this, factory).get(ItemEditViewModel::class.java)

        //User is navigated from which menagePage of items to create new one.
        val position = when (manageType) {
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

        //Get editType spinner selected to change views
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

        //Get periodType spinner selected to change views.
        viewModel.currentPeriodType.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    0 -> {
                        tvOngoingDay.visibility = View.GONE
                        spOngoingDay.visibility = View.GONE
                        tvSuspendDay.visibility = View.GONE
                        spSuspendDay.visibility = View.GONE
                    }
                    1 -> {
                        tvOngoingDay.visibility = View.VISIBLE
                        tvOngoingDay.text = "天"
                        spOngoingDay.visibility = View.VISIBLE
                        spOngoingDay.adapter = ArrayAdapter.createFromResource(
                            PublicApplication.application.applicationContext,
                            R.array.day,
                            android.R.layout.simple_spinner_dropdown_item
                        )
                        tvSuspendDay.visibility = View.GONE
                        spSuspendDay.visibility = View.GONE
                    }

                    2 -> {
                        tvOngoingDay.visibility = View.VISIBLE
                        tvOngoingDay.text = "禮拜"
                        spOngoingDay.visibility = View.VISIBLE
                        spOngoingDay.adapter = ArrayAdapter.createFromResource(
                            PublicApplication.application.applicationContext,
                            R.array.week,
                            android.R.layout.simple_spinner_dropdown_item
                        )
                        tvSuspendDay.visibility = View.GONE
                        spSuspendDay.visibility = View.GONE
                    }
                    3 -> {
                        tvOngoingDay.visibility = View.VISIBLE
                        tvOngoingDay.text = "執行幾天"
                        spOngoingDay.visibility = View.VISIBLE
                        spOngoingDay.adapter = ArrayAdapter.createFromResource(
                            PublicApplication.application.applicationContext,
                            R.array.cycle_day,
                            android.R.layout.simple_spinner_dropdown_item
                        )
                        tvSuspendDay.visibility = View.VISIBLE
                        tvSuspendDay.text = "暫停幾天"
                        spSuspendDay.visibility = View.VISIBLE
                    }
                    4 -> {
                        tvOngoingDay.visibility = View.GONE
                        spOngoingDay.visibility = View.GONE
                        tvSuspendDay.visibility = View.GONE
                        spSuspendDay.visibility = View.GONE
                    }
                }
            }
        }

        //Get timeSet spinner selected to change views
        viewModel.timePointSet.observe(viewLifecycleOwner) {
            Log.i("DataDate", it.toString())
            val adapter = TimeSetAdapter(
                TimeSetAdapter.OnclickListener {
                    viewModel.removeTimeSet(it)
                }
            )
            it.sort()
            adapter.submitList(it)
            binding.rvTimeChose.adapter = adapter
        }

        viewModel.dateSet.observe(viewLifecycleOwner) {
            binding.tvFirstDateSelected.text = it
        }

        setFragmentResultListener("GetTime") { requestKey, bundle ->
            viewModel.getTimeSet(bundle.getLong("Time"))
        }

        setFragmentResultListener("GetDate") { requestKey, bundle ->
            viewModel.getDateSet(bundle.getLong("Date"))
        }

        binding.btTimeSet.setOnClickListener {
            findNavController().navigate(
                NavigationDirections.actionGlobalCalenderTimeDialog(
                    EditTimeType.TIME
                )
            )
        }

        binding.tvFirstDateSelected.setOnClickListener {
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
                    Toast.makeText(context, "新增完成!", Toast.LENGTH_LONG).show()
                    findNavController().popBackStack()
                }
                false -> Toast.makeText(context, "有東西還沒填喔!", Toast.LENGTH_LONG).show()
            }
        }

        return binding.root
    }

    private fun checkInput(binding: ItemEditFragmentBinding): Boolean {

        return when (binding.spItemType.selectedItemPosition) {
            0 -> {
                when {
                    binding.tilDrugName.editText?.text.isNullOrEmpty() -> {
                        false
                    }
                    binding.etvDrugDose.text.isNullOrEmpty() -> {
                        false
                    }
                    binding.tvFirstDateSelected.text.isNullOrEmpty() -> {
                        false
                    }
                    binding.rvTimeChose.adapter?.itemCount == 0 -> {
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
                true
            }

            2 -> {
                when {
                    binding.tvFirstDateSelected.text.isNullOrEmpty() -> {
                        false
                    }
                    binding.rvTimeChose.adapter?.itemCount == 0 -> {
                        false
                    }
                    else -> {
                        true
                    }
                }
            }

            3 -> {
                when {
                    binding.tvFirstDateSelected.text.isNullOrEmpty() -> {
                        false
                    }
                    binding.rvTimeChose.adapter?.itemCount == 0 -> {
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