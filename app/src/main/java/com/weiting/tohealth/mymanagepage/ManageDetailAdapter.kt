package com.weiting.tohealth.mymanagepage

import android.content.Context
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
import com.weiting.tohealth.util.Util.getTimeStampToTimeInt
import com.weiting.tohealth.util.Util.setActivityType
import com.weiting.tohealth.util.Util.setDrugDrawable
import com.weiting.tohealth.util.Util.setMeasureDrawable
import com.weiting.tohealth.util.Util.toActivityType
import com.weiting.tohealth.util.Util.toCareType
import com.weiting.tohealth.util.Util.toMeasureType
import com.weiting.tohealth.util.Util.toStatus
import com.weiting.tohealth.util.Util.toStringFromPeriod
import com.weiting.tohealth.util.Util.toStringFromTimeStamp
import com.weiting.tohealth.util.Util.toUnit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ManageDetailAdapter(private val dataType: ManageType, val onClickListener: OnclickListener) :
    ListAdapter<ItemData, ItemsListViewHolder>(DiffCallBack) {

    val context: Context = PublicApplication.application.applicationContext
    val coroutineScope = CoroutineScope(Dispatchers.Main)
    val database = PublicApplication.application.firebaseDataRepository

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

    // TODO Refactor 3rd
    inner class ItemsListViewHolder(private val binding: ManageRowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemData) {
            binding.apply {
                btEdit.setOnClickListener {
                    onClickListener.onClick(getItem(position), dataType)
                }
                coroutineScope.launch {

                    val adapter = ManageDetailTimeAdapter()

                    when (dataType) {
                        ManageType.DRUG -> {
                            val data = item.DrugData
                            adapter.submitList(
                                data?.executedTime?.sortedBy {
                                    getTimeStampToTimeInt(it)
                                }
                            )

                            when (data?.executedTime?.isEmpty()) {
                                true -> {
                                    binding.tvTimeTitle.text =
                                        context.getString(R.string.setTime_empty)
                                }
                                false -> {
                                    binding.tvTimeTitle.text = context.getString(R.string.setTime)
                                }
                            }

                            rvTimeList.adapter = adapter
                            tvItemNameManage.text = data?.drugName
                            imItemIcon.setImageResource(setDrugDrawable(data?.unit))
                            tvPeriod.text = toStringFromPeriod(data?.period ?: mapOf())
                            tvTagManage.text = toStatus(data?.status)

                            tvPerTimeTitle.visibility = View.VISIBLE
                            tvDose.visibility = View.VISIBLE
                            tvDose.text = data?.dose.toString()
                            tvUnitManage.visibility = View.VISIBLE
                            tvUnitManage.text = toUnit(data?.unit)
                            tvRatioTitle.text = "剩餘藥量"

                            "${data?.stock}${toUnit(data?.unit)}".also { tvRatioNum.text = it }
                            stockProgress(
                                binding,
                                (data?.stock ?: 0L).toInt(),
                                (data?.dose ?: 0L).toInt()
                            )

                            tvUpdateTime.text = toStringFromTimeStamp(data?.lastEditTime)
                            tvCreatedTime.text = toStringFromTimeStamp(data?.createdTime)
                            tvEditorManage.text = database.getUser(data?.editor ?: "").name
                        }

                        ManageType.MEASURE -> {
                            val data = item.MeasureData
                            adapter.submitList(
                                data?.executedTime?.sortedBy {
                                    getTimeStampToTimeInt(it)
                                }
                            )

                            when (data?.executedTime?.isEmpty()) {
                                true -> {
                                    binding.tvTimeTitle.text =
                                        context.getString(R.string.setTime_empty)
                                }
                                false -> {
                                    binding.tvTimeTitle.text = context.getString(R.string.setTime)
                                }
                            }

                            rvTimeList.adapter = adapter
                            tvItemNameManage.text = toMeasureType(data?.type)
                            imItemIcon.setImageResource(setMeasureDrawable(data?.type))
                            tvPeriod.text = context.getString(R.string.executedTitle)

                            tvTagManage.text = toStatus(data?.status)

                            tvPerTimeTitle.visibility = View.GONE
                            tvDose.visibility = View.GONE
                            tvUnitManage.visibility = View.GONE
                            tvCreatedTime.text = toStringFromTimeStamp(data?.createdTime)
                            tvUpdateTime.text = toStringFromTimeStamp(data?.lastEditTime)
                            tvRatioTitle.text = context.getString(R.string.outDate)
                            tvRatioNum.text = context.getString(R.string.unLimit)
                            tvStockDayUnit.text = context.getString(R.string.unLimit)
                            pbStock.progress = 100

                            tvEditorManage.text = database.getUser(data?.editor ?: "").name
                        }

                        ManageType.ACTIVITY -> {
                            val data = item.ActivityData
                            adapter.submitList(
                                data?.executedTime?.sortedBy {
                                    getTimeStampToTimeInt(it)
                                }
                            )

                            when (data?.executedTime?.isEmpty()) {
                                true -> {
                                    binding.tvTimeTitle.text =
                                        context.getString(R.string.setTime_empty)
                                }
                                false -> {
                                    binding.tvTimeTitle.text = context.getString(R.string.setTime)
                                }
                            }

                            rvTimeList.adapter = adapter
                            tvItemNameManage.text = toActivityType(data?.type)
                            imItemIcon.setImageResource(setActivityType(data?.type))
                            tvPeriod.text = toStringFromPeriod(data?.period ?: mapOf())
                            tvTagManage.text = toStatus(data?.status)

                            tvPerTimeTitle.visibility = View.GONE
                            tvDose.visibility = View.GONE
                            tvUnitManage.visibility = View.GONE
                            tvCreatedTime.text = toStringFromTimeStamp(data?.createdTime)
                            tvUpdateTime.text = toStringFromTimeStamp(data?.lastEditTime)
                            tvRatioTitle.text = context.getString(R.string.outDate)
                            tvRatioNum.text = context.getString(R.string.unLimit)
                            tvStockDayUnit.text = context.getString(R.string.unLimit)
                            pbStock.progress = 100

                            tvEditorManage.text = database.getUser(data?.editor ?: "").name
                        }

                        ManageType.CARE -> {
                            val data = item.CareData
                            adapter.submitList(
                                data?.executeTime?.sortedBy {
                                    getTimeStampToTimeInt(it)
                                }
                            )

                            when (data?.executeTime?.isEmpty()) {
                                true -> {
                                    binding.tvTimeTitle.text =
                                        context.getString(R.string.setTime_empty)
                                }
                                false -> {
                                    binding.tvTimeTitle.text = context.getString(R.string.setTime)
                                }
                            }

                            rvTimeList.adapter = adapter
                            tvItemNameManage.text = toCareType(data?.type)
                            imItemIcon.setImageResource(R.drawable.stopwatch)
                            tvPeriod.text = toStringFromPeriod(data?.period ?: mapOf())
                            tvRatioTitle.text = context.getString(R.string.outDate)
                            tvRatioNum.text = context.getString(R.string.unLimit)
                            tvStockDayUnit.text = context.getString(R.string.unLimit)
                            pbStock.progress = 100
                            tvTagManage.text = toStatus(data?.status)

                            tvPerTimeTitle.visibility = View.GONE
                            tvDose.visibility = View.GONE
                            tvUnitManage.visibility = View.GONE

                            tvCreatedTime.text = toStringFromTimeStamp(data?.createdTime)
                            tvUpdateTime.text = toStringFromTimeStamp(data?.lastEditTime)

                            tvEditorManage.text = database.getUser(data?.editor ?: "").name
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
        holder.bind(getItem(position))
    }

    class OnclickListener(val clickListener: (itemData: ItemData, manageType: ManageType) -> Unit) {
        fun onClick(itemData: ItemData, manageType: ManageType) =
            clickListener(itemData, manageType)
    }

    private fun stockProgress(binding: ManageRowItemBinding, stock: Int, dose: Int) {
        binding.apply {
            val stillTime = stock / dose
            val context = PublicApplication.application.applicationContext
            "可執行\n${stillTime}次".also { tvStockDayUnit.text = it }
            pbStock.progress = stillTime
            when {
                stillTime < 10 -> pbStock.setIndicatorColor(context.getColor(R.color.baby_pink))
                stillTime < 50 -> pbStock.setIndicatorColor(context.getColor(R.color.pink_yellow))
                else -> pbStock.setIndicatorColor(context.getColor(R.color.areo_blue))
            }
        }
    }
}
