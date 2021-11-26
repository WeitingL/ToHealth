package com.weiting.tohealth.homepage.fastadd

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.weiting.tohealth.data.*
import com.weiting.tohealth.databinding.ItemRowBinding
import com.weiting.tohealth.util.Util.setEventType
import com.weiting.tohealth.util.Util.setDrugDrawable
import com.weiting.tohealth.util.Util.setMeasureDrawable
import com.weiting.tohealth.util.Util.toEventType
import com.weiting.tohealth.util.Util.toMeasureType
import com.weiting.tohealth.util.Util.toUnit
import java.lang.ClassCastException

const val VIEW_TYPE_DRUG = 0
const val VIEW_TYPE_MEASURE = 1
const val VIEW_TYPE_EVENT = 2

class FastAddAdapter(val onClickListener: OnclickListener) :
    ListAdapter<ItemData, RecyclerView.ViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<ItemData>() {
        override fun areItemsTheSame(oldItem: ItemData, newItem: ItemData): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: ItemData, newItem: ItemData): Boolean =
            oldItem == newItem
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).itemType) {
            ItemType.DRUG -> VIEW_TYPE_DRUG
            ItemType.MEASURE -> VIEW_TYPE_MEASURE
            ItemType.EVENT -> VIEW_TYPE_EVENT
            else -> throw Exception("Something wrong")
        }
    }

    inner class DrugViewHolder(private val binding: ItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(drug: Drug?) {
            binding.apply {
                tvName.text = drug?.drugName
                imageView.setImageResource(setDrugDrawable(drug?.unit))
                (drug?.dose.toString() + toUnit(drug?.unit)).also { tvUnit.text = it }
                ("剩餘" + drug?.stock.toString() + toUnit(drug?.unit)).also { tvStock.text = it }
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
        fun bind(event: Event?) {
            binding.apply {
                tvName.text = toEventType(event?.type)
                imageView.setImageResource(setEventType(event?.type))
                tvUnit.visibility = View.GONE
                tvStock.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_DRUG -> DrugViewHolder(
                ItemRowBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            VIEW_TYPE_MEASURE -> MeasureViewHolder(
                ItemRowBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            VIEW_TYPE_EVENT -> ActivityViewHolder(
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
                holder.bind((getItem(position).drugData))
                holder.itemView.setOnClickListener {
                    onClickListener.onClick((getItem(position)))
                }
            }
            is MeasureViewHolder -> {
                holder.bind((getItem(position).measureData))
                holder.itemView.setOnClickListener {
                    onClickListener.onClick((getItem(position)))
                }
            }
            is ActivityViewHolder -> {
                holder.bind((getItem(position).eventData))
                holder.itemView.setOnClickListener {
                    onClickListener.onClick((getItem(position)))
                }
            }
        }
    }

    class OnclickListener(val clickListener: (itemData: ItemData) -> Unit) {
        fun onClick(itemData: ItemData) = clickListener(itemData)
    }
}
