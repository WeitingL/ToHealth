package com.weiting.tohealth.mymanagepage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anychart.charts.Stock
import com.weiting.tohealth.*
import com.weiting.tohealth.data.ItemData
import com.weiting.tohealth.data.UserManager
import com.weiting.tohealth.databinding.ManageRowItemBinding
import com.weiting.tohealth.mymanagepage.ManageDetailAdapter.ItemsListViewHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ManageDetailAdapter(private val dataType: ManageType, val onClickListener: OnclickListener) :
    ListAdapter<ItemData, ItemsListViewHolder>(DiffCallBack) {

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
                    onClickListener.onClick(getItem(position), dataType)
                }
                coroutineScope.launch {

                    val adapter = ManageDetailTimeAdapter()

                    when (dataType) {
                        ManageType.DRUG -> {
                            val data = item.DrugData
                            adapter.submitList(data?.executedTime?.sortedBy {
                                getTimeStampToTimeInt(it)
                            })

                            when (data?.executedTime?.isEmpty()) {
                                true -> {
                                    binding.tvTimeTitle.text = "設定時間(空)"
                                }
                                false -> {
                                    binding.tvTimeTitle.text = "設定時間"
                                }
                            }

                            rvTimeList.adapter = adapter
                            tvItemNameManage.text = data?.drugName
                            imItemIcon.setImageResource(setDrugDrawable(data?.unit))
                            tvPeriod.text = toStringFromPeriod(data?.period!!)
                            tvTagManage.text = toStatus(data?.status)

                            tvPerTimeTitle.visibility = View.VISIBLE
                            tvDose.visibility = View.VISIBLE
                            tvDose.text = data.dose.toString()
                            tvUnitManage.visibility = View.VISIBLE
                            tvUnitManage.text = toUnit(data.unit)
                            tvRatioTitle.text = "剩餘藥量"

                            tvRatioNum.text = "${data.stock}${toUnit(data?.unit)}"
                            stockProgress(binding, data.stock.toInt(), data.dose.toInt())

                            tvUpdateTime.text = toStringFromTimeStamp(data.lastEditTime)
                            tvCreatedTime.text = toStringFromTimeStamp(data.createdTime)
                            tvEditorManage.text = database.getUser(data.editor!!).name

                        }

                        ManageType.MEASURE -> {
                            val data = item.MeasureData
                            adapter.submitList(data?.executedTime?.sortedBy {
                                getTimeStampToTimeInt(it)
                            })

                            when (data?.executedTime?.isEmpty()) {
                                true -> {
                                    binding.tvTimeTitle.text = "設定時間(空)"
                                }
                                false -> {
                                    binding.tvTimeTitle.text = "設定時間"
                                }
                            }

                            rvTimeList.adapter = adapter
                            tvItemNameManage.text = toMeasureType(data?.type)
                            imItemIcon.setImageResource(setMeasureDrawable(data?.type))
                            tvPeriod.text = "每天執行"

                            tvTagManage.text = toStatus(data?.status)

                            tvPerTimeTitle.visibility = View.GONE
                            tvDose.visibility = View.GONE
                            tvUnitManage.visibility = View.GONE
                            tvCreatedTime.text = toStringFromTimeStamp(data?.createdTime)
                            tvUpdateTime.text = toStringFromTimeStamp(data?.lastEditTime)
                            tvRatioTitle.text = "剩餘天數"
                            tvRatioNum.text = "無期限"
                            tvStockDayUnit.text = "無期限"
                            pbStock.progress = 100

                            tvEditorManage.text = database.getUser(data?.editor!!).name

                        }

                        ManageType.ACTIVITY -> {
                            val data = item.ActivityData
                            adapter.submitList(data?.executedTime?.sortedBy {
                                getTimeStampToTimeInt(it)
                            })

                            when (data?.executedTime?.isEmpty()) {
                                true -> {
                                    binding.tvTimeTitle.text = "設定時間(空)"
                                }
                                false -> {
                                    binding.tvTimeTitle.text = "設定時間"
                                }
                            }

                            rvTimeList.adapter = adapter
                            tvItemNameManage.text = toActivityType(data?.type)
                            imItemIcon.setImageResource(setActivityType(data?.type))
                            tvPeriod.text = toStringFromPeriod(data?.period!!)
                            tvTagManage.text = toStatus(data?.status)

                            tvPerTimeTitle.visibility = View.GONE
                            tvDose.visibility = View.GONE
                            tvUnitManage.visibility = View.GONE
                            tvCreatedTime.text = toStringFromTimeStamp(data?.createdTime)
                            tvUpdateTime.text = toStringFromTimeStamp(data.lastEditTime)
                            tvRatioTitle.text = "剩餘天數"
                            tvRatioNum.text = "無期限"
                            tvStockDayUnit.text = "無期限"
                            pbStock.progress = 100

                            tvEditorManage.text = database.getUser(data.editor!!).name

                        }

                        ManageType.CARE -> {
                            val data = item.CareData
                            adapter.submitList(data?.executeTime?.sortedBy {
                                getTimeStampToTimeInt(it)
                            })

                            when (data?.executeTime?.isEmpty()) {
                                true -> {
                                    binding.tvTimeTitle.text = "設定時間(空)"
                                }
                                false -> {
                                    binding.tvTimeTitle.text = "設定時間"
                                }
                            }

                            rvTimeList.adapter = adapter
                            tvItemNameManage.text = toCareType(data?.type)
                            imItemIcon.setImageResource(R.drawable.stopwatch)
                            tvPeriod.text = toStringFromPeriod(data?.period!!)
                            tvRatioTitle.text = "剩餘天數"
                            tvRatioNum.text = "無期限"
                            tvStockDayUnit.text = "無期限"
                            pbStock.progress = 100
                            tvTagManage.text = toStatus(data?.status)

                            tvPerTimeTitle.visibility = View.GONE
                            tvDose.visibility = View.GONE
                            tvUnitManage.visibility = View.GONE

                            tvCreatedTime.text = toStringFromTimeStamp(data?.createdTime)
                            tvUpdateTime.text = toStringFromTimeStamp(data.lastEditTime)

                            tvEditorManage.text = database.getUser(data.editor!!).name

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
            tvStockDayUnit.text = "可執行\n${stillTime}次"
            pbStock.progress = stillTime
            when {
                stillTime < 10 -> pbStock.setIndicatorColor(context.getColor(R.color.baby_pink))
                stillTime < 50 -> pbStock.setIndicatorColor(context.getColor(R.color.pink_yellow))
                else -> pbStock.setIndicatorColor(context.getColor(R.color.areo_blue))
            }
        }
    }
}

