package com.weiting.tohealth.homepage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.weiting.tohealth.data.Drug
import com.weiting.tohealth.databinding.HomecardNexttaskRowItemBinding
import com.weiting.tohealth.toUnit

class NextTaskCardAdapter() : ListAdapter<Drug, NextTaskCardAdapter.HomecardNexttaskViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<Drug>() {
        override fun areItemsTheSame(oldItem: Drug, newItem: Drug): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: Drug, newItem: Drug): Boolean =
            oldItem == newItem
    }

    inner class HomecardNexttaskViewHolder(private val binding: HomecardNexttaskRowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(drug: Drug) {
            binding.apply {
                tvName.text = drug.drugName
                tvUnit.text = drug.dose.toString() + toUnit(drug.unit)
                tvStock.text = "剩餘" + drug.stock.toString() + toUnit(drug.unit)
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NextTaskCardAdapter.HomecardNexttaskViewHolder {
        return HomecardNexttaskViewHolder(
            HomecardNexttaskRowItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NextTaskCardAdapter.HomecardNexttaskViewHolder, position: Int) {
        return holder.bind(getItem(position))
    }
}