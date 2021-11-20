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
                    tvItemTypeTitleUpdate.text = "藥品品項"
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
                    tvItemTypeTitleUpdate.text = "活動項目"
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
                    tvItemTypeTitleUpdate.text = "測量項目"
                    tvItemNameUpdate.text = toMeasureType(itemData.MeasureData?.type)
                    tvStartTimeUpdate.text = toDateFromTimeStamp(itemData.MeasureData?.createdTime)
                    clUnitUpdate.visibility = View.GONE
                    clStockUpdate.visibility = View.GONE
                    clPeriodUpdate.visibility = View.GONE
                    setExecuteTime(itemData, manageType, binding, viewModel)

                    setStatus(itemData.MeasureData?.status!!, binding)
                }

                ManageType.CARE -> {
                    tvItemTypeTitleUpdate.text = "關懷項目"
                    tvItemNameUpdate.text = toCareType(itemData.CareData?.type)
                    tvStartTimeUpdate.text = toDateFromTimeStamp(itemData.CareData?.startDate)
                    tvOriginPeriod.text =
                        "原先設定: ${toPeriod(itemData.CareData?.period?.get("type")!!)}"
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
                        tvOngoingDayUpdate.text = "禮拜日"
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
                        tvOngoingDayUpdate.text = "執行幾天"
                        spOngoingDayUpdate.adapter = ArrayAdapter.createFromResource(
                            PublicApplication.application.applicationContext,
                            R.array.cycle_day,
                            android.R.layout.simple_spinner_dropdown_item
                        )

                        tvSuspendDayUpdate.visibility = View.VISIBLE
                        spSuspendDayUpdate.visibility = View.VISIBLE
                        tvSuspendDayUpdate.text = "暫停幾天"
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
            Toast.makeText(context, "已成功更新!", Toast.LENGTH_LONG).show()
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
        setFragmentResultListener("GetTime") { requestKey, bundle ->
            viewModel.getTimeSet(bundle.getLong("Time"))
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
                            tvOriginN.text = "原先設定: ${toDay(itemData.DrugData.period["N"]!!)}"
                        }
                        2 -> {
                            spOngoingDayUpdate.setSelection(itemData.DrugData.period["N"]!!)
                            tvOriginN.text = "原先設定: ${toWeek(itemData.DrugData.period["N"]!!)}"
                        }
                        3 -> {
                            spOngoingDayUpdate.setSelection(itemData.DrugData.period["N"]!!)
                            tvOriginN.text =
                                "原先設定: ${toCycleValue(itemData.DrugData.period["N"]!!)}"

                            spSuspendDayUpdate.setSelection(itemData.DrugData.period["X"]!!)
                            tvOriginX.text =
                                "原先設定: ${toCycleValue(itemData.DrugData.period["X"]!!)}"
                        }
                    }
                }
                ManageType.ACTIVITY -> {
                    when (itemData.ActivityData?.period?.get("type")) {
                        1 -> {
                            spOngoingDayUpdate.setSelection(itemData.ActivityData.period["N"]!!)
                            tvOriginN.text = "原先設定: ${toDay(itemData.ActivityData.period["N"]!!)}"
                        }
                        2 -> {
                            spOngoingDayUpdate.setSelection(itemData.ActivityData.period["N"]!!)
                            tvOriginN.text = "原先設定: ${toWeek(itemData.ActivityData.period["N"]!!)}"
                        }
                        3 -> {
                            spOngoingDayUpdate.setSelection(itemData.ActivityData.period["N"]!!)
                            tvOriginN.text =
                                "原先設定: ${toCycleValue(itemData.ActivityData.period["N"]!!)}"

                            spSuspendDayUpdate.setSelection(itemData.ActivityData.period["X"]!!)
                            tvOriginX.text =
                                "原先設定: ${toCycleValue(itemData.ActivityData.period["X"]!!)}"
                        }
                    }
                }
                ManageType.CARE -> {
                    when (itemData.CareData?.period?.get("type")) {
                        1 -> {
                            spOngoingDayUpdate.setSelection(itemData.CareData.period["N"]!!)
                            tvOriginN.text = "原先設定: ${toDay(itemData.CareData.period["N"]!!)}"
                        }
                        2 -> {
                            spOngoingDayUpdate.setSelection(itemData.CareData.period["N"]!!)
                            tvOriginN.text = "原先設定: ${toWeek(itemData.CareData.period["N"]!!)}"
                        }
                        3 -> {
                            spOngoingDayUpdate.setSelection(itemData.CareData.period["N"]!!)
                            tvOriginN.text =
                                "原先設定: ${toCycleValue(itemData.CareData.period["N"]!!)}"

                            spSuspendDayUpdate.setSelection(itemData.CareData.period["X"]!!)
                            tvOriginX.text =
                                "原先設定: ${toCycleValue(itemData.CareData.period["X"]!!)}"
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