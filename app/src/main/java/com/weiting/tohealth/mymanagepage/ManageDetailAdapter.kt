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
import com.weiting.tohealth.data.ItemType
import com.weiting.tohealth.data.Result
import com.weiting.tohealth.databinding.ManageRowItemBinding
import com.weiting.tohealth.mymanagepage.ManageDetailAdapter.ItemsListViewHolder
import com.weiting.tohealth.util.Util.getTimeStampToTimeInt
import com.weiting.tohealth.util.Util.setEventType
import com.weiting.tohealth.util.Util.setDrugDrawable
import com.weiting.tohealth.util.Util.setMeasureDrawable
import com.weiting.tohealth.util.Util.toEventType
import com.weiting.tohealth.util.Util.toCareType
import com.weiting.tohealth.util.Util.toMeasureType
import com.weiting.tohealth.util.Util.toStatus
import com.weiting.tohealth.util.Util.toStringFromPeriod
import com.weiting.tohealth.util.Util.getTimeStampToDateAndTimeString
import com.weiting.tohealth.util.Util.toUnit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ManageDetailAdapter(val onClickListener: OnclickListener) :
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

    inner class ItemsListViewHolder(private val binding: ManageRowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemData) {
            binding.apply {
                btEdit.setOnClickListener {
                    onClickListener.onClick(getItem(position))
                }
                coroutineScope.launch {

                    val adapter = ManageDetailTimeAdapter()

                    when (item.itemType) {
                        ItemType.DRUG -> {
                            val data = item.drugData
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
                            tvRatioTitle.text = context.getString(R.string.stockTitle)

                            "${data?.stock}${toUnit(data?.unit)}".also { tvRatioNum.text = it }
                            stockProgress(
                                binding,
                                (data?.stock ?: 0L).toInt(),
                                (data?.dose ?: 0L).toInt()
                            )

                            tvUpdateTime.text = getTimeStampToDateAndTimeString(data?.lastEditTime)
                            tvCreatedTime.text = getTimeStampToDateAndTimeString(data?.createdTime)

                            val user = when(val result = database.getUser(data?.editor ?: "")){
                                is Result.Success -> result.data
                                else -> null
                            }

                            tvEditorManage.text = user?.name
                        }

                        ItemType.MEASURE -> {
                            val data = item.measureData
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
                            tvCreatedTime.text = getTimeStampToDateAndTimeString(data?.createdTime)
                            tvUpdateTime.text = getTimeStampToDateAndTimeString(data?.lastEditTime)
                            tvRatioTitle.text = context.getString(R.string.outDate)
                            tvRatioNum.text = context.getString(R.string.unLimit)
                            tvStockDayUnit.text = context.getString(R.string.unLimit)
                            pbStock.progress = 100

                            val user = when(val result = database.getUser(data?.editor ?: "")){
                                is Result.Success -> result.data
                                else -> null
                            }
                            tvEditorManage.text = user?.name
                        }

                        ItemType.EVENT -> {
                            val data = item.eventData
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
                            tvItemNameManage.text = toEventType(data?.type)
                            imItemIcon.setImageResource(setEventType(data?.type))
                            tvPeriod.text = toStringFromPeriod(data?.period ?: mapOf())
                            tvTagManage.text = toStatus(data?.status)

                            tvPerTimeTitle.visibility = View.GONE
                            tvDose.visibility = View.GONE
                            tvUnitManage.visibility = View.GONE
                            tvCreatedTime.text = getTimeStampToDateAndTimeString(data?.createdTime)
                            tvUpdateTime.text = getTimeStampToDateAndTimeString(data?.lastEditTime)
                            tvRatioTitle.text = context.getString(R.string.outDate)
                            tvRatioNum.text = context.getString(R.string.unLimit)
                            tvStockDayUnit.text = context.getString(R.string.unLimit)
                            pbStock.progress = 100

                            val user = when(val result = database.getUser(data?.editor ?: "")){
                                is Result.Success -> result.data
                                else -> null
                            }
                            tvEditorManage.text = user?.name
                        }

                        ItemType.CARE -> {
                            val data = item.careData
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

                            tvCreatedTime.text = getTimeStampToDateAndTimeString(data?.createdTime)
                            tvUpdateTime.text = getTimeStampToDateAndTimeString(data?.lastEditTime)

                            val user = when(val result = database.getUser(data?.editor ?: "")){
                                is Result.Success -> result.data
                                else -> null
                            }
                            tvEditorManage.text = user?.name
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

    class OnclickListener(val clickListener: (itemData: ItemData) -> Unit) {
        fun onClick(itemData: ItemData) =
            clickListener(itemData)
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
