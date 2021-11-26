package com.weiting.tohealth.itemeditpage

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
import com.weiting.tohealth.*
import com.weiting.tohealth.data.*
import com.weiting.tohealth.databinding.ItemUpdateFragmentBinding
import com.weiting.tohealth.factory.ItemUpdateViewModelFactory
import com.weiting.tohealth.mymanagepage.ManageDetailTimeAdapter
import com.weiting.tohealth.timeset.EditTimeType
import com.weiting.tohealth.timeset.GET_TIME
import com.weiting.tohealth.timeset.TIME
import com.weiting.tohealth.util.Util.getTimeStampToTimeInt
import com.weiting.tohealth.util.Util.toEventType
import com.weiting.tohealth.util.Util.toCareType
import com.weiting.tohealth.util.Util.toCycleValue
import com.weiting.tohealth.util.Util.getTimeStampToDateString
import com.weiting.tohealth.util.Util.toDay
import com.weiting.tohealth.util.Util.toMeasureType
import com.weiting.tohealth.util.Util.toPeriod
import com.weiting.tohealth.util.Util.toUnit
import com.weiting.tohealth.util.Util.toWeek

class ItemUpdateFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = ItemUpdateFragmentBinding.inflate(inflater, container, false)
        val userInfo = ItemUpdateFragmentArgs.fromBundle(requireArguments()).userInfo
        val itemData = ItemUpdateFragmentArgs.fromBundle(requireArguments()).itemData
        val factory = ItemUpdateViewModelFactory(
            PublicApplication.application.firebaseDataRepository,
            itemData,
            userInfo
        )
        val viewModel = ViewModelProvider(this, factory).get(ItemUpdateViewModel::class.java)

        /*
        Set the Constraint layout visibility and Basic Info ->
        GetViewModel Value ->
        Set the value by data period
         */
        binding.apply {
            when (itemData.itemType) {
                ItemType.DRUG -> {
                    tvItemTypeTitleUpdate.text = getString(R.string.DrugItem)
                    tvItemNameUpdate.text = itemData.drugData?.drugName
                    tvStartTimeUpdate.text = getTimeStampToDateString(itemData.drugData?.startDate)
                    spUnitUpdate.setSelection(itemData.drugData?.unit ?: 0)
                    "原先設定: ${toUnit(itemData.drugData?.unit)}".also { tvOriginUnit.text = it }
                    etvDrugDoseUpdate.setText(itemData.drugData?.dose.toString())

                    "原先設定: ${itemData.drugData?.dose}".also { tvOriginDose.text = it }
                    "原先設定: ${toPeriod(itemData.drugData?.period?.get("type") ?: 0)}".also { tvOriginPeriod.text = it }

                    etvStockUpdate.setText(itemData.drugData?.stock.toString())
                    "原先設定: ${itemData.drugData?.stock}".also { tvOriginStock.text = it }

                    spPeriodUpdate.setSelection(itemData.drugData?.period?.get("type") ?: 0)
                    viewModel.setPeriodType(itemData.drugData?.period?.get("type") ?: 0)
                    setExecuteTime(itemData, binding, viewModel)

                    setStatus(itemData.drugData?.status ?: 0, binding)
                }

                ItemType.EVENT -> {
                    tvItemTypeTitleUpdate.text = getString(R.string.EventItem)
                    tvItemNameUpdate.text = toEventType(itemData.eventData?.type)
                    tvStartTimeUpdate.text = getTimeStampToDateString(itemData.eventData?.startDate)
                    clUnitUpdate.visibility = View.GONE
                    clStockUpdate.visibility = View.GONE
                    "原先設定: ${toPeriod(itemData.eventData?.period?.get("type") ?: 0)}".also { tvOriginPeriod.text = it }
                    spPeriodUpdate.setSelection(itemData.eventData?.period?.get("type") ?: 0)
                    viewModel.setPeriodType(itemData.eventData?.period?.get("type") ?: 0)
                    setExecuteTime(itemData, binding, viewModel)

                    setStatus(itemData.eventData?.status ?: 0, binding)
                }

                ItemType.MEASURE -> {
                    tvItemTypeTitleUpdate.text = getString(R.string.MeasureItem)
                    tvItemNameUpdate.text = toMeasureType(itemData.measureData?.type)
                    tvStartTimeUpdate.text = getTimeStampToDateString(itemData.measureData?.createdTime)
                    clUnitUpdate.visibility = View.GONE
                    clStockUpdate.visibility = View.GONE
                    clPeriodUpdate.visibility = View.GONE
                    setExecuteTime(itemData, binding, viewModel)

                    setStatus(itemData.measureData?.status!!, binding)
                }

                ItemType.CARE -> {
                    tvItemTypeTitleUpdate.text = getString(R.string.CareItem)
                    tvItemNameUpdate.text = toCareType(itemData.careData?.type)
                    tvStartTimeUpdate.text = getTimeStampToDateString(itemData.careData?.startDate)
                    "原先設定: ${toPeriod(itemData.careData?.period?.get("type") ?: 0)}".also { tvOriginPeriod.text = it }
                    clUnitUpdate.visibility = View.GONE
                    clStockUpdate.visibility = View.GONE

                    spPeriodUpdate.setSelection(itemData.careData?.period?.get("type") ?: 0)
                    viewModel.setPeriodType(itemData.careData?.period?.get("type") ?: 0)
                    setExecuteTime(itemData, binding, viewModel)

                    setStatus(itemData.careData?.status ?: 0, binding)
                }
            }
        }

        viewModel.periodType.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    1 -> {
                        tvOngoingDayUpdate.visibility = View.VISIBLE
                        spOngoingDayUpdate.visibility = View.VISIBLE
                        tvOngoingDayUpdate.text = getString(R.string.dayTitle)
                        spOngoingDayUpdate.adapter = ArrayAdapter.createFromResource(
                            PublicApplication.application.applicationContext,
                            R.array.day,
                            android.R.layout.simple_spinner_dropdown_item
                        )

                        tvSuspendDayUpdate.visibility = View.GONE
                        spSuspendDayUpdate.visibility = View.GONE

                        tvOriginX.visibility = View.GONE
                        tvOriginN.visibility = View.VISIBLE

                        setValueByPeriod(itemData, binding)
                    }
                    2 -> {
                        tvOngoingDayUpdate.visibility = View.VISIBLE
                        spOngoingDayUpdate.visibility = View.VISIBLE
                        tvOngoingDayUpdate.text = getString(R.string.weekTitle)
                        spOngoingDayUpdate.adapter = ArrayAdapter.createFromResource(
                            PublicApplication.application.applicationContext,
                            R.array.week,
                            android.R.layout.simple_spinner_dropdown_item
                        )

                        tvSuspendDayUpdate.visibility = View.GONE
                        spSuspendDayUpdate.visibility = View.GONE

                        tvOriginX.visibility = View.GONE
                        tvOriginN.visibility = View.VISIBLE

                        setValueByPeriod(itemData, binding)
                    }
                    3 -> {
                        tvOngoingDayUpdate.visibility = View.VISIBLE
                        spOngoingDayUpdate.visibility = View.VISIBLE
                        tvOngoingDayUpdate.text = getString(R.string.executeDayTitle)
                        spOngoingDayUpdate.adapter = ArrayAdapter.createFromResource(
                            PublicApplication.application.applicationContext,
                            R.array.cycle_day,
                            android.R.layout.simple_spinner_dropdown_item
                        )

                        tvSuspendDayUpdate.visibility = View.VISIBLE
                        spSuspendDayUpdate.visibility = View.VISIBLE
                        tvSuspendDayUpdate.text = getString(R.string.suspendDayTitle)
                        spSuspendDayUpdate.adapter = ArrayAdapter.createFromResource(
                            PublicApplication.application.applicationContext,
                            R.array.cycle_day,
                            android.R.layout.simple_spinner_dropdown_item
                        )

                        tvOriginX.visibility = View.VISIBLE
                        tvOriginN.visibility = View.VISIBLE

                        setValueByPeriod(itemData, binding)
                    }
                    else -> {
                        tvOngoingDayUpdate.visibility = View.GONE
                        spOngoingDayUpdate.visibility = View.GONE
                        tvSuspendDayUpdate.visibility = View.GONE
                        spSuspendDayUpdate.visibility = View.GONE

                        tvOriginN.visibility = View.GONE
                        tvOriginX.visibility = View.GONE
                    }
                }
            }
        }

        binding.gbStatusChoose.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.status_going -> {
                    viewModel.getStatus(0)
                }
                R.id.status_pending -> {
                    viewModel.getStatus(1)
                }
                R.id.status_stop -> {
                    viewModel.getStatus(2)
                }
            }
        }

        binding.spPeriodUpdate.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    viewModel.setPeriodType(p2)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }

        binding.btUpdateItem.setOnClickListener {
            viewModel.updateItem(binding)
            viewModel.startSetAlarmForTodoList()
            Toast.makeText(context, getString(R.string.upDateSuccess), Toast.LENGTH_LONG).show()
            findNavController().popBackStack()
        }

        // Get Time from pick!
        viewModel.timePointSet.observe(viewLifecycleOwner) { it ->
            val timeSetAdapter = TimeSetAdapter(
                TimeSetAdapter.OnclickListener {
                    viewModel.removeTimeSet(it)
                }
            )
            timeSetAdapter.submitList(it)
            binding.rvTimeChoseUpdate.adapter = timeSetAdapter
        }
        setFragmentResultListener(GET_TIME) { _, bundle ->
            viewModel.getTimeSet(bundle.getLong(TIME))
        }
        binding.btTimeSetUpdate.setOnClickListener {
            findNavController().navigate(
                NavigationDirections.actionGlobalCalenderTimeDialog(
                    EditTimeType.TIME
                )
            )
        }

        return binding.root
    }

    private fun setValueByPeriod(
        itemData: ItemData,
        binding: ItemUpdateFragmentBinding
    ) {
        binding.apply {
            when (itemData.itemType) {
                ItemType.DRUG -> {
                    when (itemData.drugData?.period?.get(TYPE)) {
                        1 -> {
                            spOngoingDayUpdate.setSelection(itemData.drugData.period[N] ?: 0)
                            "原先設定: ${toDay(itemData.drugData.period[N] ?: 0)}".also { tvOriginN.text = it }
                        }
                        2 -> {
                            spOngoingDayUpdate.setSelection(itemData.drugData.period[N] ?: 0)
                            "原先設定: ${toWeek(itemData.drugData.period[N] ?: 0)}".also { tvOriginN.text = it }
                        }
                        3 -> {
                            spOngoingDayUpdate.setSelection(itemData.drugData.period[N] ?: 0)
                            "原先設定: ${toCycleValue(itemData.drugData.period[N] ?: 0)}".also { tvOriginN.text = it }

                            spSuspendDayUpdate.setSelection(itemData.drugData.period[X] ?: 0)
                            "原先設定: ${toCycleValue(itemData.drugData.period[X] ?: 0)}".also { tvOriginX.text = it }
                        }
                    }
                }
                ItemType.EVENT -> {
                    when (itemData.eventData?.period?.get(TYPE)) {
                        1 -> {
                            spOngoingDayUpdate.setSelection(itemData.eventData.period[N] ?: 0)
                            "原先設定: ${toDay(itemData.eventData.period[N] ?: 0)}".also { tvOriginN.text = it }
                        }
                        2 -> {
                            spOngoingDayUpdate.setSelection(itemData.eventData.period[N] ?: 0)
                            "原先設定: ${toWeek(itemData.eventData.period[N] ?: 0)}".also { tvOriginN.text = it }
                        }
                        3 -> {
                            spOngoingDayUpdate.setSelection(itemData.eventData.period[N] ?: 0)
                            "原先設定: ${toCycleValue(itemData.eventData.period[N] ?: 0)}".also { tvOriginN.text = it }

                            spSuspendDayUpdate.setSelection(itemData.eventData.period[X] ?: 0)
                            "原先設定: ${toCycleValue(itemData.eventData.period[X] ?: 0)}".also { tvOriginX.text = it }
                        }
                    }
                }
                ItemType.CARE -> {
                    when (itemData.careData?.period?.get(TYPE)) {
                        1 -> {
                            spOngoingDayUpdate.setSelection(itemData.careData.period[N] ?: 0)
                            "原先設定: ${toDay(itemData.careData.period[N] ?: 0)}".also { tvOriginN.text = it }
                        }
                        2 -> {
                            spOngoingDayUpdate.setSelection(itemData.careData.period[N] ?: 0)
                            "原先設定: ${toWeek(itemData.careData.period[N] ?: 0)}".also { tvOriginN.text = it }
                        }
                        3 -> {
                            spOngoingDayUpdate.setSelection(itemData.careData.period[N] ?: 0)
                            "原先設定: ${toCycleValue(itemData.careData.period[N] ?: 0)}".also { tvOriginN.text = it }

                            spSuspendDayUpdate.setSelection(itemData.careData.period[X] ?: 0)
                            "原先設定: ${toCycleValue(itemData.careData.period[X] ?: 0)}".also { tvOriginX.text = it }
                        }
                    }
                }
            }
        }
    }

    private fun setExecuteTime(
        itemData: ItemData,
        binding: ItemUpdateFragmentBinding,
        viewModel: ItemUpdateViewModel
    ) {
        val adapter = ManageDetailTimeAdapter()
        when (itemData.itemType) {
            ItemType.DRUG -> {
                itemData.drugData?.executedTime?.forEach {
                    viewModel.getTimeSet(it.toDate().time)
                }
                adapter.submitList(
                    itemData.drugData?.executedTime?.sortedBy {
                        getTimeStampToTimeInt(it)
                    }
                )
            }
            ItemType.CARE -> {
                itemData.careData?.executedTime?.forEach {
                    viewModel.getTimeSet(it.toDate().time)
                }
                adapter.submitList(
                    itemData.careData?.executedTime?.sortedBy {
                        getTimeStampToTimeInt(it)
                    }
                )
            }
            ItemType.EVENT -> {
                itemData.eventData?.executedTime?.forEach {
                    viewModel.getTimeSet(it.toDate().time)
                }
                adapter.submitList(
                    itemData.eventData?.executedTime?.sortedBy {
                        getTimeStampToTimeInt(it)
                    }
                )
            }
            ItemType.MEASURE -> {
                itemData.measureData?.executedTime?.forEach {
                    viewModel.getTimeSet(it.toDate().time)
                }
                adapter.submitList(
                    itemData.measureData?.executedTime?.sortedBy {
                        getTimeStampToTimeInt(it)
                    }
                )
            }
        }
        binding.rvOriginalTimeSet.adapter = adapter
    }

    private fun setStatus(
        status: Int,
        binding: ItemUpdateFragmentBinding
    ) {
        when (status) {
            0 -> binding.statusGoing.isChecked = true
            1 -> binding.statusPending.isChecked = true
            2 -> binding.statusStop.isChecked = true
        }
    }
}
