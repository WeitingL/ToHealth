package com.weiting.tohealth.mystatisticpage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.weiting.tohealth.databinding.*
import com.weiting.tohealth.mystatisticpage.activitychart.ActivityTimeScaleAdapter
import com.weiting.tohealth.mystatisticpage.drugchart.DrugTimeScaleAdapter
import java.lang.ClassCastException

const val STATISTIC_VIEWTYPE_DRUGLOGITEM = 0
const val STATISTIC_VIEWTYPE_MEASURELOGITEM = 1
const val STATISTIC_VIEWTYPE_ACTIVITYLOGITEM = 2
const val STATISTIC_VIEWTYPE_CARELOGITEM = 3
const val STATISTIC_VIEWTYPE_BUTTON = 4

class StatisticDetailAdapter : ListAdapter<LogItem, RecyclerView.ViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<LogItem>() {
        override fun areItemsTheSame(oldItem: LogItem, newItem: LogItem): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: LogItem, newItem: LogItem): Boolean =
            oldItem == newItem

    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is LogItem.DrugLogItem -> STATISTIC_VIEWTYPE_DRUGLOGITEM
            is LogItem.Bottom -> STATISTIC_VIEWTYPE_BUTTON
            is LogItem.CareLogItem -> STATISTIC_VIEWTYPE_CARELOGITEM
            is LogItem.ActivityLogItem -> STATISTIC_VIEWTYPE_ACTIVITYLOGITEM
        }
    }

    inner class ActivityLogItemViewHolder(private val binding: StatisticRowActivityBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(item: LogItem.ActivityLogItem){
                binding.tvItemName.text = item.itemName
                val adapter = ActivityTimeScaleAdapter()
                adapter.submitList(item.list)
                binding.rvActivityTimeLine.adapter = adapter
            }
    }

    inner class MeasureLogItemViewHolder(private val binding: StatisticRowMeasureBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    inner class CareLogItemViewHolder(private val binding: StatisticRowCareBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    inner class DrugLogItemViewHolder(private val binding: StatisticRowDrugBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LogItem.DrugLogItem) {
            binding.tvItemName.text = item.itemName
            val adapter = DrugTimeScaleAdapter()
            adapter.submitList(item.list)
            binding.rvDrugTimeLine.adapter = adapter
        }
    }

    inner class ExportLogDataViewHolder(private val binding: CardviewBottombuttonRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.tvBottomButton.text = "資料輸出"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            STATISTIC_VIEWTYPE_DRUGLOGITEM -> DrugLogItemViewHolder(
                StatisticRowDrugBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            STATISTIC_VIEWTYPE_CARELOGITEM -> CareLogItemViewHolder(
                StatisticRowCareBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            STATISTIC_VIEWTYPE_BUTTON -> ExportLogDataViewHolder(
                CardviewBottombuttonRowBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            STATISTIC_VIEWTYPE_ACTIVITYLOGITEM -> ActivityLogItemViewHolder(
                StatisticRowActivityBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            else -> throw ClassCastException("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DrugLogItemViewHolder -> {
                holder.bind(getItem(position) as LogItem.DrugLogItem)
            }

            is ExportLogDataViewHolder -> {
                holder.bind()
            }

            is ActivityLogItemViewHolder ->{
                holder.bind(getItem(position) as LogItem.ActivityLogItem)
            }
        }
    }

}