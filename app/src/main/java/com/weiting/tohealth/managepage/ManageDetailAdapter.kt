package com.weiting.tohealth.managepage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.weiting.tohealth.databinding.ManageRowItemBinding
import com.weiting.tohealth.managepage.ManageDetailAdapter.ItemsListViewHolder

class ManageDetailAdapter : ListAdapter<ManageDetailItems, ItemsListViewHolder>(DiffCallBack) {

    object DiffCallBack : DiffUtil.ItemCallback<ManageDetailItems>() {
        override fun areItemsTheSame(
            oldItem: ManageDetailItems,
            newItem: ManageDetailItems
        ): Boolean = oldItem === newItem

        override fun areContentsTheSame(
            oldItem: ManageDetailItems,
            newItem: ManageDetailItems
        ): Boolean = oldItem == newItem

    }

    inner class ItemsListViewHolder(private val binding: ManageRowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(){

            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsListViewHolder {
        return ItemsListViewHolder(
            ManageRowItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemsListViewHolder, position: Int) {
        return holder.bind()
    }


}