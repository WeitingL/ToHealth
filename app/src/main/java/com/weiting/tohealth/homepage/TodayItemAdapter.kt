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
import com.weiting.tohealth.util.Util.setActivityType
import com.weiting.tohealth.util.Util.setDrugDrawable
import com.weiting.tohealth.util.Util.setMeasureDrawable
import com.weiting.tohealth.util.Util.toActivityType
import com.weiting.tohealth.util.Util.toCareType
import com.weiting.tohealth.util.Util.toMeasureType
import com.weiting.tohealth.util.Util.toUnit
import java.lang.ClassCastException

const val ITEM_VIEWTYPE_TIME = 0
const val ITEM_VIEWTYPE_DRUG = 1
const val ITEM_VIEWTYPE_MEASURE = 2
const val ITEM_VIEWTYPE_ACTIVITY = 3
const val ITEM_VIEWTYPE_CARE = 4

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
            is ItemDataType.TimeType -> ITEM_VIEWTYPE_TIME
            is ItemDataType.DrugType -> ITEM_VIEWTYPE_DRUG
            is ItemDataType.MeasureType -> ITEM_VIEWTYPE_MEASURE
            is ItemDataType.ActivityType -> ITEM_VIEWTYPE_ACTIVITY
            is ItemDataType.CareType -> ITEM_VIEWTYPE_CARE
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

    inner class ActivityViewHolder(private val binding: ItemRowHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(activity: Activity?) {
            binding.apply {
                tvName.text = toActivityType(activity?.type)
                imageView.setImageResource(setActivityType(activity?.type))
                tvUnit.visibility = View.GONE
            }
        }
    }

    inner class CareViewHolder(private val binding: ItemRowHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(care: Care?) {
            binding.apply {
                val editor = if (care?.editor == UserManager.UserInformation.id) {
                    "${UserManager.UserInformation.name}"
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
            ITEM_VIEWTYPE_TIME -> TimeViewHolder(
                TimeRowBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            ITEM_VIEWTYPE_DRUG -> DrugViewHolder(
                ItemRowHomeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            ITEM_VIEWTYPE_MEASURE -> MeasureViewHolder(
                ItemRowHomeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            ITEM_VIEWTYPE_ACTIVITY -> ActivityViewHolder(
                ItemRowHomeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            ITEM_VIEWTYPE_CARE -> CareViewHolder(
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

//        Log.i("positionAdapter", position.toString())

        when (holder) {
            is TimeViewHolder -> {
                holder.bind((getItem(position) as ItemDataType.TimeType).time)
            }
            is DrugViewHolder -> {
                holder.bind((getItem(position) as ItemDataType.DrugType).drug.DrugData)
            }
            is MeasureViewHolder -> {
                holder.bind((getItem(position) as ItemDataType.MeasureType).measure.MeasureData)
            }
            is ActivityViewHolder -> {
                holder.bind((getItem(position) as ItemDataType.ActivityType).activity.ActivityData)
            }
            is CareViewHolder -> {
                holder.bind((getItem(position) as ItemDataType.CareType).care.CareData)
            }
        }
    }

    // TODO Empty space need to add new things.
}
