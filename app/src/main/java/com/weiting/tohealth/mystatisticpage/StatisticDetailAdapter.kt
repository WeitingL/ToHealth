package com.weiting.tohealth.mystatisticpage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.weiting.tohealth.databinding.CardviewBottombuttonRowBinding
import com.weiting.tohealth.databinding.MystatisticItemFagmentBinding
import com.weiting.tohealth.databinding.StatisticRowItemBinding
import java.lang.ClassCastException

const val STATISTIC_VIEWTYPE_LOGITEM = 0
const val STATISTIC_VIEWTYPE_BUTTON = 1

class StatisticDetailAdapter : ListAdapter<LogItem, RecyclerView.ViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<LogItem>() {
        override fun areItemsTheSame(oldItem: LogItem, newItem: LogItem): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: LogItem, newItem: LogItem): Boolean =
            oldItem == newItem

    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is LogItem.Item -> STATISTIC_VIEWTYPE_LOGITEM
            is LogItem.Bottom -> STATISTIC_VIEWTYPE_BUTTON
        }
    }

    inner class LogItemViewHolder(private val binding: StatisticRowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    inner class ExportLogDataViewHolder(private val binding: CardviewBottombuttonRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(){
                binding.tvBottomButton.text = "資料輸出"
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            STATISTIC_VIEWTYPE_LOGITEM -> LogItemViewHolder(
                StatisticRowItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            STATISTIC_VIEWTYPE_BUTTON -> ExportLogDataViewHolder(
                CardviewBottombuttonRowBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            else -> throw ClassCastException("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder){
            is LogItemViewHolder -> {

            }

            is ExportLogDataViewHolder ->{
                holder.bind()
            }
        }
    }

}