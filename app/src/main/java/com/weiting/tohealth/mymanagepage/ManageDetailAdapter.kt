package com.weiting.tohealth.mymanagepage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.weiting.tohealth.*
import com.weiting.tohealth.data.ItemData
import com.weiting.tohealth.databinding.ManageRowItemBinding
import com.weiting.tohealth.mymanagepage.ManageDetailAdapter.ItemsListViewHolder

class ManageDetailAdapter(private val dataType: ManageType) :
    ListAdapter<ItemData, ItemsListViewHolder>(DiffCallBack) {

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
        fun bind(item: ItemData) {
            binding.apply {
                when (dataType) {
                    ManageType.DRUG -> {
                        val data = item.DrugData

                        tvItemNameManage.text = data?.drugName
                        tvDose.text = data?.dose.toString()
                        tvUnitManage.text = toUnit(data?.unit)
                        tvStockManage.text = "剩餘" + data?.stock + toUnit(data?.dose)
                        tvEndDate.text = toEndDate(data?.endDate)
                        tvTime.text = toTimeFromTimeStamp(data?.firstTimePerDay)
                        tvTagManage.text = toStatus(data?.status)
                        tvCreatedTime.text = "創建時間: " + toStringFromTimeStamp(data?.createTime)
                        tvEditorManage.text = "編輯者: " + data?.editor
                    }

                    ManageType.MEASURE -> {
                        val data = item.MeasureData

                        tvItemNameManage.text = toMeasureType(data?.type)
                        tvDose.visibility = View.GONE
                        tvUnitManage.visibility = View.GONE
                        tvStockManage.visibility = View.GONE
                        tvEndDate.visibility = View.GONE
                        tvTime.text = toTimeFromTimeStamp(data?.firstTimePerDay)
                        tvTagManage.text = toStatus(data?.status)
                        tvCreatedTime.text = "創建時間: " + toStringFromTimeStamp(data?.createTime)
                        tvEditorManage.text = "編輯者: " + data?.editor
                    }

                    ManageType.ACTIVITY -> {
                        val data = item.ActivityData

                        tvItemNameManage.text = toMeasureType(data?.type)
                        tvDose.visibility = View.GONE
                        tvUnitManage.visibility = View.GONE
                        tvStockManage.visibility = View.GONE
                        tvEndDate.text = toEndDate(data?.endDate)
                        tvTime.text = toTimeFromTimeStamp(data?.firstTimePerDay)
                        tvTagManage.text = toStatus(data?.status)
                        tvCreatedTime.text = "創建時間: " + toStringFromTimeStamp(data?.createTime)
                        tvEditorManage.text = "編輯者: " + data?.editor
                    }

                    ManageType.CARE -> {
                        val data = item.CareData

                        tvItemNameManage.text = toCareType(data?.type)
                        tvDose.visibility = View.GONE
                        tvUnitManage.visibility = View.GONE
                        tvStockManage.visibility = View.GONE
                        tvEndDate.text = toEndDate(data?.endDate)
                        tvTime.text = toTimeFromTimeStamp(data?.firstTimePerDay)
                        tvTagManage.text = toStatus(data?.status)
                        tvCreatedTime.text = "創建時間: " + toStringFromTimeStamp(data?.createTime)
                        tvEditorManage.text = "編輯者: " + data?.editor
                    }
                }
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

