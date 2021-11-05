package com.weiting.tohealth.mystatisticpage

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.enums.*
import com.weiting.tohealth.databinding.*
import com.weiting.tohealth.mystatisticpage.activitychart.ActivityTimeScaleAdapter
import com.weiting.tohealth.mystatisticpage.drugchart.DrugTimeScaleAdapter
import com.weiting.tohealth.toStringFromTimeStamp
import com.weiting.tohealth.toTimeFromTimeStamp
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
        fun bind(item: LogItem.ActivityLogItem) {
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
        fun bind(item: LogItem.CareLogItem) {
            binding.tvItemName.text = item.itemName

            binding.acCareLogs.setProgressBar(binding.progressBar2)
            val cartesian = AnyChart.column()
            val data = mutableListOf<DataEntry>()

            item.list.forEach {
                data.add(ValueEntry(toStringFromTimeStamp(it.createTime), it.emotion, it.note))
            }

            val column = cartesian.column(data)
            column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0)
                .offsetY(2000)
                .format("心情指數: {%Value} \\n 心情:{%Note}")

            cartesian.animation(true)
            cartesian.yScale().minimum(0)
            cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }")

            cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
            cartesian.interactivity().hoverMode(HoverMode.BY_X)

            cartesian.yAxis(0).title("分數")

            cartesian.xZoom().setToPointsCount(4, false, null)
            cartesian.xScroller(true)
            cartesian.xScroller().position(ChartScrollerPosition.BEFORE_AXES)
            cartesian.xScroller().fill("#FFFFFF")
            cartesian.xScroller().selectedFill("#7A7979", 4)
            cartesian.xScroller().allowRangeChange(false)

            binding.acCareLogs.setChart(cartesian)
        }
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

            is ActivityLogItemViewHolder -> {
                holder.bind(getItem(position) as LogItem.ActivityLogItem)
            }
            is CareLogItemViewHolder -> {
                holder.bind(getItem(position) as LogItem.CareLogItem)
            }
        }
    }
}

private class ValueEntry internal constructor(
    x: String,
    n: Int,
    note: String
) : ValueDataEntry(x, n) {
    init {
        setValue("x", x)
        setValue("n", n)
        setValue("note", note)
    }
}