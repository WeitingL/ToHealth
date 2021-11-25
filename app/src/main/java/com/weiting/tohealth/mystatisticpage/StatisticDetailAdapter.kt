package com.weiting.tohealth.mystatisticpage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.data.Set
import com.anychart.enums.*
import com.weiting.tohealth.PublicApplication
import com.weiting.tohealth.R
import com.weiting.tohealth.data.X
import com.weiting.tohealth.data.Y
import com.weiting.tohealth.data.Z
import com.weiting.tohealth.databinding.*
import com.weiting.tohealth.mystatisticpage.activitychart.EventTimeScaleAdapter
import com.weiting.tohealth.mystatisticpage.drugchart.DrugTimeScaleAdapter
import com.weiting.tohealth.util.Util.toMeasureType
import com.weiting.tohealth.util.Util.toStringFromTimeStamp
import com.weiting.tohealth.util.Util.toUnitForMeasure
import java.lang.ClassCastException

const val VIEW_TYPE_DRUG_LOG = 0
const val VIEW_TYPE_MEASURE_LOG = 1
const val VIEW_TYPE_EVENT_LOG = 2
const val VIEW_TYPE_CARE_LOG = 3
const val VIEW_TYPE_BUTTON = 4

class StatisticDetailAdapter : ListAdapter<LogItem, RecyclerView.ViewHolder>(DiffCallback) {

    private val context = PublicApplication.application.applicationContext

    object DiffCallback : DiffUtil.ItemCallback<LogItem>() {
        override fun areItemsTheSame(oldItem: LogItem, newItem: LogItem): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: LogItem, newItem: LogItem): Boolean =
            oldItem == newItem
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is LogItem.DrugLogItem -> VIEW_TYPE_DRUG_LOG
            is LogItem.Bottom -> VIEW_TYPE_BUTTON
            is LogItem.CareLogItem -> VIEW_TYPE_CARE_LOG
            is LogItem.EventLogItem -> VIEW_TYPE_EVENT_LOG
            is LogItem.MeasureLogItem -> VIEW_TYPE_MEASURE_LOG
        }
    }

    inner class ActivityLogItemViewHolder(private val binding: StatisticRowEventBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LogItem.EventLogItem) {
            binding.tvItemName.text = item.itemName
            val adapter = EventTimeScaleAdapter()
            adapter.submitList(item.list)
            binding.rvActivityTimeLine.adapter = adapter
        }
    }

    inner class MeasureLogItemViewHolder(private val binding: StatisticRowMeasureBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LogItem.MeasureLogItem) {
            binding.tvItemName.text = toMeasureType(item.type)
//            Log.i("holder", item.toString())
            binding.acMainChart.setProgressBar(binding.progressBar3)

            // Get the dataList
            val list = mutableListOf<DataEntry>()
            item.list.forEach {
                list.add(
                    MeasureValueEntry(
                        toStringFromTimeStamp(it.createTime),
                        it.record[X],
                        it.record[Y],
                        it.record[Z]
                    )
                )
            }

            // Blood Pressure chart
            if (item.type == 0) {
                val cartesian = AnyChart.cartesian()
                val set = Set.instantiate()
                set.data(list)
                val data = set.mapAs("{x: 'x', high : 'n', low: 'm'}")

                cartesian.rangeColumn(data)
                    .color("#634444")
                cartesian.xAxis(true)
                    .yAxis(true)
                    .yGrid(true)
                    .yMinorGrid(true)

                cartesian.tooltip().titleFormat("登錄時間 {%x}")
                    .format("收縮壓: {%n} mmHg \\n 舒張壓: {%m} mmHg \\n 心律: {%y} bpm")

                cartesian.xZoom().setToPointsCount(6, false, null)
                cartesian.xScroller(true)
                cartesian.xScroller().position(ChartScrollerPosition.BEFORE_AXES)
                cartesian.xScroller().fill("#FFFFFF")
                cartesian.xScroller().selectedFill("#F0F0F0", 90)
                cartesian.xScroller().allowRangeChange(false)
                binding.acMainChart.setChart(cartesian)

                // Others chart
            } else {
                val barChart = AnyChart.column()
                val set = Set.instantiate()
                set.data(list)
                val data = set.mapAs("{x: 'x', n : 'n'}")
                barChart.column(data)
                    .color("#634444")

                barChart.animation(true)

                barChart.yScale().stackMode(ScaleStackMode.VALUE)

                barChart.yAxis(0).title(toUnitForMeasure(item.type))
                barChart.xAxis(0).overlapMode(LabelsOverlapMode.NO_OVERLAP)

                barChart.tooltip()
                    .titleFormat("登錄時間 {%x}")
                    .format("{%n}")

                binding.acMainChart.setChart(barChart)
            }
        }
    }

    inner class CareLogItemViewHolder(private val binding: StatisticRowCareBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LogItem.CareLogItem) {

            binding.tvItemName.text = item.itemName

            binding.acCareLogs.setProgressBar(binding.progressBar2)
            val cartesian = AnyChart.column()
            val data = mutableListOf<DataEntry>()

            item.list.forEach {
                data.add(CareValueEntry(toStringFromTimeStamp(it.createTime), it.emotion, it.note))
            }

            val column = cartesian.column(data)
                .color("#634444")
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

            cartesian.xZoom().setToPointsCount(6, false, null)
            cartesian.xScroller(true)
            cartesian.xScroller().position(ChartScrollerPosition.BEFORE_AXES)
            cartesian.xScroller().fill("#FFFFFF")
            cartesian.xScroller().selectedFill("#F0F0F0", 90)
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
            binding.tvBottomButton.text = context.getString(R.string.dataExtract)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_DRUG_LOG -> DrugLogItemViewHolder(
                StatisticRowDrugBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            VIEW_TYPE_CARE_LOG -> CareLogItemViewHolder(
                StatisticRowCareBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            VIEW_TYPE_BUTTON -> ExportLogDataViewHolder(
                CardviewBottombuttonRowBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            VIEW_TYPE_EVENT_LOG -> ActivityLogItemViewHolder(
                StatisticRowEventBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            VIEW_TYPE_MEASURE_LOG -> MeasureLogItemViewHolder(
                StatisticRowMeasureBinding.inflate(
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
                holder.bind(getItem(position) as LogItem.EventLogItem)
            }

            is CareLogItemViewHolder -> {
                holder.bind(getItem(position) as LogItem.CareLogItem)
            }

            is MeasureLogItemViewHolder -> {
                holder.bind(getItem(position) as LogItem.MeasureLogItem)
            }
        }
    }
}

private class CareValueEntry(
    x: String,
    n: Int?,
    note: String?
) : ValueDataEntry(x, n) {
    init {
        setValue("x", x)
        setValue("n", n)
        setValue("note", note)
    }
}

private class MeasureValueEntry(
    x: String,
    n: Int?,
    m: Int?,
    y: Int?,
) : ValueDataEntry(x, n) {
    init {
        setValue("x", x)
        setValue("n", n)
        setValue("m", m)
        setValue("y", y)
    }
}
