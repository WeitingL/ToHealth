package com.weiting.tohealth.homepage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.weiting.tohealth.data.Drug
import com.weiting.tohealth.databinding.DetailTodoItemRowBinding

class DailyItemsAdapter() : ListAdapter<Drug, DailyItemsAdapter.ItemListViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<Drug>() {
        override fun areItemsTheSame(oldItem: Drug, newItem: Drug): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: Drug, newItem: Drug): Boolean =
            oldItem == newItem
    }

    inner class ItemListViewHolder(private val binding: DetailTodoItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(drug: Drug) {
            binding.apply {
                tvName.text = drug.drugName
                tvUnit.text = drug.unit.toString()
                tvStock.text = drug.stock.toString()
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DailyItemsAdapter.ItemListViewHolder {
        return ItemListViewHolder(
            DetailTodoItemRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DailyItemsAdapter.ItemListViewHolder, position: Int) {
        return holder.bind(getItem(position))
    }
}