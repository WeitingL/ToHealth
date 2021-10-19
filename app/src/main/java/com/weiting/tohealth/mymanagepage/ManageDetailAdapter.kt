package com.weiting.tohealth.mymanagepage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.weiting.tohealth.data.ItemData
import com.weiting.tohealth.databinding.ManageRowItemBinding
import com.weiting.tohealth.mymanagepage.ManageDetailAdapter.ItemsListViewHolder
import com.weiting.tohealth.toEndDate
import com.weiting.tohealth.toStatus
import com.weiting.tohealth.toUnit

class ManageDetailAdapter : ListAdapter<ItemData, ItemsListViewHolder>(DiffCallBack) {

    object DiffCallBack : DiffUtil.ItemCallback<ItemData>() {
        override fun areItemsTheSame(
            oldItem: ItemData,
            newItem: ItemData
        ): Boolean = oldItem === newItem

        override fun areContentsTheSame(
            oldItem: ItemData,
            newItem: ItemData
        ): Boolean = oldItem == newItem

    }

    inner class ItemsListViewHolder(private val binding: ManageRowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(item: ItemData){
                val data = item.DrugData
                binding.apply {
                    tvItemNameManage.text = data?.drugName
                    tvDose.text = data?.dose.toString()
                    tvUnitManage.text = toUnit(data?.dose)
                    tvStockManage.text = "剩餘" + data?.stock + toUnit(data?.dose)
                    tvEndDate.text = toEndDate(data?.endDate)
                    tvTime.text = data?.firstTimePerDay.toString()
                    tvTagManage.text = toStatus(data?.status)
                }
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
        return holder.bind(getItem(position))
    }


}