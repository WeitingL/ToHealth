package com.weiting.tohealth.homepage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.weiting.tohealth.*
import com.weiting.tohealth.data.*
import com.weiting.tohealth.databinding.ItemRowHomeBinding
import com.weiting.tohealth.databinding.TimeRowBinding
import com.weiting.tohealth.util.Util.setEventType
import com.weiting.tohealth.util.Util.setDrugDrawable
import com.weiting.tohealth.util.Util.setMeasureDrawable
import com.weiting.tohealth.util.Util.toEventType
import com.weiting.tohealth.util.Util.toCareType
import com.weiting.tohealth.util.Util.toMeasureType
import com.weiting.tohealth.util.Util.toUnit
import java.lang.ClassCastException

const val VIEW_TYPE_TIME = 0
const val VIEW_TYPE_DRUG = 1
const val VIEW_TYPE_MEASURE = 2
const val VIEW_TYPE_EVENT = 3
const val VIEW_TYPE_CARE = 4

class TodayItemAdapter :
    ListAdapter<ItemDataType, RecyclerView.ViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<ItemDataType>() {
        override fun areItemsTheSame(oldItem: ItemDataType, newItem: ItemDataType): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: ItemDataType, newItem: ItemDataType): Boolean =
            oldItem == newItem
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ItemDataType.TimeType -> VIEW_TYPE_TIME
            is ItemDataType.DrugType -> VIEW_TYPE_DRUG
            is ItemDataType.MeasureType -> VIEW_TYPE_MEASURE
            is ItemDataType.EventType -> VIEW_TYPE_EVENT
            is ItemDataType.CareType -> VIEW_TYPE_CARE
            else -> throw Exception("Get wrong view type ${getItem(position)}")
        }
    }

    inner class TimeViewHolder(private val binding: TimeRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(time: String) {
            binding.tvName.text = time
        }
    }

    inner class DrugViewHolder(private val binding: ItemRowHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(drug: Drug?) {
            binding.apply {
                tvName.text = drug?.drugName
                imageView.setImageResource(setDrugDrawable(drug?.unit))
                (drug?.dose.toString() + toUnit(drug?.unit)).also { tvUnit.text = it }
            }
        }
    }

    inner class MeasureViewHolder(private val binding: ItemRowHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(measure: Measure?) {
            binding.apply {
                tvName.text = toMeasureType(measure?.type)
                imageView.setImageResource(setMeasureDrawable(measure?.type))
                tvUnit.visibility = View.GONE
            }
        }
    }

    inner class EventViewHolder(private val binding: ItemRowHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event?) {
            binding.apply {
                tvName.text = toEventType(event?.type)
                imageView.setImageResource(setEventType(event?.type))
                tvUnit.visibility = View.GONE
            }
        }
    }

    inner class CareViewHolder(private val binding: ItemRowHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(care: Care?) {
            binding.apply {
                val editor = if (care?.editor == UserManager.UserInfo.id) {
                    "${UserManager.UserInfo.name}"
                } else {
                    "others"
                }

                tvName.text = toCareType(care?.type)
                imageView.setImageResource(R.drawable.stopwatch)
                "提出人$editor".also { tvUnit.text = it }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_TIME -> TimeViewHolder(
                TimeRowBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            VIEW_TYPE_DRUG -> DrugViewHolder(
                ItemRowHomeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            VIEW_TYPE_MEASURE -> MeasureViewHolder(
                ItemRowHomeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            VIEW_TYPE_EVENT -> EventViewHolder(
                ItemRowHomeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            VIEW_TYPE_CARE -> CareViewHolder(
                ItemRowHomeBinding.inflate(
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
            is TimeViewHolder -> {
                holder.bind((getItem(position) as ItemDataType.TimeType).time)
            }
            is DrugViewHolder -> {
                holder.bind((getItem(position) as ItemDataType.DrugType).drug.drugData)
            }
            is MeasureViewHolder -> {
                holder.bind((getItem(position) as ItemDataType.MeasureType).measure.measureData)
            }
            is EventViewHolder -> {
                holder.bind((getItem(position) as ItemDataType.EventType).event.eventData)
            }
            is CareViewHolder -> {
                holder.bind((getItem(position) as ItemDataType.CareType).care.careData)
            }
        }
    }

    // TODO Empty space need to add new things.
}
