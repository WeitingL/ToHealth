package com.weiting.tohealth.mymanagepage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.weiting.tohealth.*
import com.weiting.tohealth.data.ItemData
import com.weiting.tohealth.data.UserManager
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

                val adapter = ManageDetailTimeAdapter()

                when (dataType) {
                    ManageType.DRUG -> {
                        val data = item.DrugData
                        adapter.submitList(data?.executeTime)

                        rvTimeList.adapter = adapter
                        tvItemNameManage.text = data?.drugName
                        imItemIcon.setImageResource(setDrugDrawable(data?.unit))
                        tvPeriod.text = toStringFromPeriod(data?.period!!)

                        tvPerTimeTitle.visibility = View.VISIBLE
                        tvDose.visibility = View.VISIBLE
                        tvDose.text = data?.dose?.toString()
                        tvUnitManage.visibility = View.VISIBLE
                        tvUnitManage.text = toUnit(data?.unit)
                        tvRatioTitle.text = "剩餘藥量"
                        tvRatioNum.text = "${data.stock}${toUnit(data?.unit)}"

                        tvCreatedTime.text = toStringFromTimeStamp(data?.createTime)

                        tvEditorManage.text = if (data?.editor == UserManager.userId) {
                            UserManager.name
                        } else {
                            "others"
                        }

                    }

                    ManageType.MEASURE -> {
                        val data = item.MeasureData
                        adapter.submitList(data?.executeTime)

                        rvTimeList.adapter = adapter
                        tvItemNameManage.text = toMeasureType(data?.type)
                        imItemIcon.setImageResource(setMeasureDrawable(data?.type))
                        tvPeriod.text = "每天執行"

                        tvPerTimeTitle.visibility = View.GONE
                        tvDose.visibility = View.GONE
                        tvUnitManage.visibility = View.GONE
                        tvCreatedTime.text = toStringFromTimeStamp(data?.createTime)
                        tvRatioTitle.text = "剩餘天數"
                        tvRatioNum.text = "無期限"

                        tvEditorManage.text = if (data?.editor == UserManager.userId) {
                            UserManager.name
                        } else {
                            "others"
                        }

                    }

                    ManageType.ACTIVITY -> {
                        val data = item.ActivityData
                        adapter.submitList(data?.executeTime)

                        rvTimeList.adapter = adapter
                        tvItemNameManage.text = toActivityType(data?.type)
                        imItemIcon.setImageResource(setActivityType(data?.type))
                        tvPeriod.text = toStringFromPeriod(data?.period!!)

                        tvPerTimeTitle.visibility = View.GONE
                        tvDose.visibility = View.GONE
                        tvUnitManage.visibility = View.GONE
                        tvCreatedTime.text = toStringFromTimeStamp(data?.createTime)
                        tvRatioTitle.text = "剩餘天數"
                        tvRatioNum.text = "無期限"

                        tvEditorManage.text = if (data?.editor == UserManager.userId) {
                            UserManager.name
                        } else {
                            "others"
                        }

                    }

                    ManageType.CARE -> {
                        val data = item.CareData
                        adapter.submitList(data?.executeTime)

                        rvTimeList.adapter = adapter
                        tvItemNameManage.text = toCareType(data?.type)
                        imItemIcon.setImageResource(R.drawable.stopwatch)
                        tvPeriod.text = toStringFromPeriod(data?.period!!)
                        tvRatioTitle.text = "剩餘天數"
                        tvRatioNum.text = "無期限"

                        tvPerTimeTitle.visibility = View.GONE
                        tvDose.visibility = View.GONE
                        tvUnitManage.visibility = View.GONE

                        tvCreatedTime.text = toStringFromTimeStamp(data?.createTime)

                        tvEditorManage.text = if (data?.editor == UserManager.userId) {
                            UserManager.name
                        } else {
                            "others"
                        }

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

