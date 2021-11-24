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
import com.weiting.tohealth.mymanagepage.ManageType
import com.weiting.tohealth.timeset.EditTimeType
import com.weiting.tohealth.timeset.GET_TIME
import com.weiting.tohealth.timeset.TIME
import com.weiting.tohealth.util.Util.getTimeStampToTimeInt
import com.weiting.tohealth.util.Util.toActivityType
import com.weiting.tohealth.util.Util.toCareType
import com.weiting.tohealth.util.Util.toCycleValue
import com.weiting.tohealth.util.Util.toDateFromTimeStamp
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
        val manageType = ItemUpdateFragmentArgs.fromBundle(requireArguments()).manageType
        val userInfo = ItemUpdateFragmentArgs.fromBundle(requireArguments()).userInfo
        val itemData = ItemUpdateFragmentArgs.fromBundle(requireArguments()).itemData
        val factory = ItemUpdateViewModelFactory(
            PublicApplication.application.firebaseDataRepository,
            itemData,
            manageType,
            userInfo
        )
        val viewModel = ViewModelProvider(this, factory).get(ItemUpdateViewModel::class.java)


        /*
        Set the Constraint layout visibility and Basic Info ->
        GetViewModel Value ->
        Set the value by data period
         */
        binding.apply {
            when (manageType) {
                ManageType.DRUG -> {
                    tvItemTypeTitleUpdate.text = getString(R.string.DrugItem)
                    tvItemNameUpdate.text = itemData.DrugData?.drugName
                    tvStartTimeUpdate.text = toDateFromTimeStamp(itemData.DrugData?.startDate)
                    spUnitUpdate.setSelection(itemData.DrugData?.unit!!)
                    tvOriginUnit.text = "原先設定: ${toUnit(itemData.DrugData?.unit)}"
                    etvDrugDoseUpdate.setText(itemData.DrugData.dose.toString())

                    tvOriginDose.text = "原先設定: ${itemData.DrugData?.dose}"
                    tvOriginPeriod.text = "原先設定: ${toPeriod(itemData.DrugData?.period["type"]!!)}"

                    etvStockUpdate.setText(itemData.DrugData.stock.toString())
                    tvOriginStock.text = "原先設定: ${itemData.DrugData.stock}"

                    spPeriodUpdate.setSelection(itemData.DrugData.period["type"]!!)
                    viewModel.setPeriodType(itemData.DrugData.period["type"]!!)
                    setExecuteTime(itemData, manageType, binding, viewModel)

                    setStatus(itemData.DrugData?.status!!, binding)
                }

                ManageType.ACTIVITY -> {
                    tvItemTypeTitleUpdate.text = getString(R.string.ActivityItem)
                    tvItemNameUpdate.text = toActivityType(itemData.ActivityData?.type)
                    tvStartTimeUpdate.text = toDateFromTimeStamp(itemData.ActivityData?.startDate)
                    clUnitUpdate.visibility = View.GONE
                    clStockUpdate.visibility = View.GONE
                    tvOriginPeriod.text =
                        "原先設定: ${toPeriod(itemData.ActivityData?.period?.get("type")!!)}"
                    spPeriodUpdate.setSelection(itemData.ActivityData?.period?.get("type")!!)
                    viewModel.setPeriodType(itemData.ActivityData.period["type"]!!)
                    setExecuteTime(itemData, manageType, binding, viewModel)

                    setStatus(itemData.ActivityData.status!!, binding)
                }

                ManageType.MEASURE -> {
                    tvItemTypeTitleUpdate.text = getString(R.string.MeasureItem)
                    tvItemNameUpdate.text = toMeasureType(itemData.MeasureData?.type)
                    tvStartTimeUpdate.text = toDateFromTimeStamp(itemData.MeasureData?.createdTime)
                    clUnitUpdate.visibility = View.GONE
                    clStockUpdate.visibility = View.GONE
                    clPeriodUpdate.visibility = View.GONE
                    setExecuteTime(itemData, manageType, binding, viewModel)

                    setStatus(itemData.MeasureData?.status!!, binding)
                }

                ManageType.CARE -> {
                    tvItemTypeTitleUpdate.text = getString(R.string.CareItem)
                    tvItemNameUpdate.text = toCareType(itemData.CareData?.type)
                    tvStartTimeUpdate.text = toDateFromTimeStamp(itemData.CareData?.startDate)
                    "原先設定: ${toPeriod(itemData.CareData?.period?.get("type")!!)}".also { tvOriginPeriod.text = it }
                    clUnitUpdate.visibility = View.GONE
                    clStockUpdate.visibility = View.GONE

                    spPeriodUpdate.setSelection(itemData.CareData?.period?.get("type")!!)
                    viewModel.setPeriodType(itemData.CareData.period["type"]!!)
                    setExecuteTime(itemData, manageType, binding, viewModel)

                    setStatus(itemData.CareData.status!!, binding)
                }
            }
        }

        viewModel.periodType.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    1 -> {
                        tvOngoingDayUpdate.visibility = View.VISIBLE
                        spOngoingDayUpdate.visibility = View.VISIBLE
                        tvOngoingDayUpdate.text = "日"
                        spOngoingDayUpdate.adapter = ArrayAdapter.createFromResource(
                            PublicApplication.application.applicationContext,
                            R.array.day,
                            android.R.layout.simple_spinner_dropdown_item
                        )

                        tvSuspendDayUpdate.visibility = View.GONE
                        spSuspendDayUpdate.visibility = View.GONE

                        tvOriginX.visibility = View.GONE
                        tvOriginN.visibility = View.VISIBLE

                        setValueByPeriod(itemData, manageType, binding)
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

                        setValueByPeriod(itemData, manageType, binding)
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

                        setValueByPeriod(itemData, manageType, binding)
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

        binding.gbStatusChoose.setOnCheckedChangeListener { radioGroup, i ->
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

        //Get Time from pick!
        viewModel.timePointSet.observe(viewLifecycleOwner) { it ->
            val timeSetAdapter = TimeSetAdapter(TimeSetAdapter.OnclickListener {
                viewModel.removeTimeSet(it)
            })
            timeSetAdapter.submitList(it)
            binding.rvTimeChoseUpdate.adapter = timeSetAdapter
        }
        setFragmentResultListener(GET_TIME) { requestKey, bundle ->
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
        manageType: ManageType,
        binding: ItemUpdateFragmentBinding
    ) {
        binding.apply {
            when (manageType) {
                ManageType.DRUG -> {
                    when (itemData.DrugData?.period?.get("type")) {
                        1 -> {
                            spOngoingDayUpdate.setSelection(itemData.DrugData.period["N"]!!)
                            "原先設定: ${toDay(itemData.DrugData.period["N"]!!)}".also { tvOriginN.text = it }
                        }
                        2 -> {
                            spOngoingDayUpdate.setSelection(itemData.DrugData.period["N"]!!)
                            "原先設定: ${toWeek(itemData.DrugData.period["N"]!!)}".also { tvOriginN.text = it }
                        }
                        3 -> {
                            spOngoingDayUpdate.setSelection(itemData.DrugData.period["N"]!!)
                            "原先設定: ${toCycleValue(itemData.DrugData.period["N"]!!)}".also { tvOriginN.text = it }

                            spSuspendDayUpdate.setSelection(itemData.DrugData.period["X"]!!)
                            "原先設定: ${toCycleValue(itemData.DrugData.period["X"]!!)}".also { tvOriginX.text = it }
                        }
                    }
                }
                ManageType.ACTIVITY -> {
                    when (itemData.ActivityData?.period?.get("type")) {
                        1 -> {
                            spOngoingDayUpdate.setSelection(itemData.ActivityData.period["N"]!!)
                            "原先設定: ${toDay(itemData.ActivityData.period["N"]!!)}".also { tvOriginN.text = it }
                        }
                        2 -> {
                            spOngoingDayUpdate.setSelection(itemData.ActivityData.period["N"]!!)
                            "原先設定: ${toWeek(itemData.ActivityData.period["N"]!!)}".also { tvOriginN.text = it }
                        }
                        3 -> {
                            spOngoingDayUpdate.setSelection(itemData.ActivityData.period["N"]!!)
                            "原先設定: ${toCycleValue(itemData.ActivityData.period["N"]!!)}".also { tvOriginN.text = it }

                            spSuspendDayUpdate.setSelection(itemData.ActivityData.period["X"]!!)
                            "原先設定: ${toCycleValue(itemData.ActivityData.period["X"]!!)}".also { tvOriginX.text = it }
                        }
                    }
                }
                ManageType.CARE -> {
                    when (itemData.CareData?.period?.get("type")) {
                        1 -> {
                            spOngoingDayUpdate.setSelection(itemData.CareData.period["N"]!!)
                            "原先設定: ${toDay(itemData.CareData.period["N"]!!)}".also { tvOriginN.text = it }
                        }
                        2 -> {
                            spOngoingDayUpdate.setSelection(itemData.CareData.period["N"]!!)
                            "原先設定: ${toWeek(itemData.CareData.period["N"]!!)}".also { tvOriginN.text = it }
                        }
                        3 -> {
                            spOngoingDayUpdate.setSelection(itemData.CareData.period["N"]!!)
                            "原先設定: ${toCycleValue(itemData.CareData.period["N"]!!)}".also { tvOriginN.text = it }

                            spSuspendDayUpdate.setSelection(itemData.CareData.period["X"]!!)
                            "原先設定: ${toCycleValue(itemData.CareData.period["X"]!!)}".also { tvOriginX.text = it }
                        }
                    }
                }
            }
        }
    }

    private fun setExecuteTime(
        itemData: ItemData,
        manageType: ManageType,
        binding: ItemUpdateFragmentBinding,
        viewModel: ItemUpdateViewModel
    ) {
        val adapter = ManageDetailTimeAdapter()
        when (manageType) {
            ManageType.DRUG -> {
                itemData.DrugData?.executedTime?.forEach {
                    viewModel.getTimeSet(it.toDate().time)
                }
                adapter.submitList(itemData.DrugData?.executedTime?.sortedBy {
                    getTimeStampToTimeInt(it)
                })
            }
            ManageType.CARE -> {
                itemData.CareData?.executeTime?.forEach {
                    viewModel.getTimeSet(it.toDate().time)
                }
                adapter.submitList(itemData.CareData?.executeTime?.sortedBy {
                    getTimeStampToTimeInt(it)
                })
            }
            ManageType.ACTIVITY -> {
                itemData.ActivityData?.executedTime?.forEach {
                    viewModel.getTimeSet(it.toDate().time)
                }
                adapter.submitList(itemData.ActivityData?.executedTime?.sortedBy {
                    getTimeStampToTimeInt(it)
                })
            }
            ManageType.MEASURE -> {
                itemData.MeasureData?.executedTime?.forEach {
                    viewModel.getTimeSet(it.toDate().time)
                }
                adapter.submitList(itemData.MeasureData?.executedTime?.sortedBy {
                    getTimeStampToTimeInt(it)
                })
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