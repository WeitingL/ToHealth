package com.weiting.tohealth.homepage.fastadd

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.weiting.tohealth.*
import com.weiting.tohealth.data.*
import com.weiting.tohealth.databinding.ItemRowBinding
import com.weiting.tohealth.homepage.ItemDataType
import java.lang.ClassCastException

const val FASTADD_VIEWTYPE_DRUG = 0
const val FASTADD_VIEWTYPE_MEASURE = 1
const val FASTADD_VIEWTYPE_ACTIVITY = 2

class FastAddAdapter(val onClickListener: OnclickListener) :
    ListAdapter<FastAddItem, RecyclerView.ViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<FastAddItem>() {
        override fun areItemsTheSame(oldItem: FastAddItem, newItem: FastAddItem): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: FastAddItem, newItem: FastAddItem): Boolean =
            oldItem == newItem
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is FastAddItem.DrugItem -> FASTADD_VIEWTYPE_DRUG
            is FastAddItem.MeasureItem -> FASTADD_VIEWTYPE_MEASURE
            is FastAddItem.ActivityItem -> FASTADD_VIEWTYPE_ACTIVITY
        }
    }

    inner class DrugViewHolder(private val binding: ItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(drug: Drug?) {
            binding.apply {
                tvName.text = drug?.drugName
                imageView.setImageResource(setDrugDrawable(drug?.unit))
                tvUnit.text = drug?.dose.toString() + toUnit(drug?.unit)
                tvStock.text = "剩餘" + drug?.stock.toString() + toUnit(drug?.unit)
            }
        }
    }

    inner class MeasureViewHolder(private val binding: ItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(measure: Measure?) {
            binding.apply {
                tvName.text = toMeasureType(measure?.type)
                imageView.setImageResource(setMeasureDrawable(measure?.type))
                tvUnit.visibility = View.GONE
                tvStock.visibility = View.GONE
            }
        }
    }

    inner class ActivityViewHolder(private val binding: ItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(activity: Activity?) {
            binding.apply {
                tvName.text = toActivityType(activity?.type)
                imageView.setImageResource(setActivityType(activity?.type))
                tvUnit.visibility = View.GONE
                tvStock.visibility = View.GONE

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            FASTADD_VIEWTYPE_DRUG -> DrugViewHolder(
                ItemRowBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            FASTADD_VIEWTYPE_MEASURE -> MeasureViewHolder(
                ItemRowBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            FASTADD_VIEWTYPE_ACTIVITY -> ActivityViewHolder(
                ItemRowBinding.inflate(
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
            is DrugViewHolder -> {
                holder.bind((getItem(position) as FastAddItem.DrugItem).drug)
                holder.itemView.setOnClickListener {
                    onClickListener.onClick((getItem(position) as FastAddItem.DrugItem))
                }
            }
            is MeasureViewHolder -> {
                holder.bind((getItem(position) as FastAddItem.MeasureItem).measure)
                holder.itemView.setOnClickListener {
                    onClickListener.onClick((getItem(position) as FastAddItem.MeasureItem))
                }
            }
            is ActivityViewHolder -> {
                holder.bind((getItem(position) as FastAddItem.ActivityItem).activity)
                holder.itemView.setOnClickListener {
                    onClickListener.onClick((getItem(position) as FastAddItem.ActivityItem))
                }
            }
        }
    }

    class OnclickListener(val clickListener: (fastAddItem: FastAddItem) -> Unit) {
        fun onClick(fastAddItem: FastAddItem) = clickListener(fastAddItem)
    }

}