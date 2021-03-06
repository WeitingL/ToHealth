package com.weiting.tohealth.mystatisticpage.eventchart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.weiting.tohealth.databinding.StatisticDrugTimeRowBinding
import com.weiting.tohealth.mystatisticpage.ResultInDate
import com.weiting.tohealth.mystatisticpage.drugchart.DrugResultScaleAdapter
import com.weiting.tohealth.util.Util.getTimeStampToDateWithoutYString

class EventTimeScaleAdapter :
    ListAdapter<ResultInDate, EventTimeScaleAdapter.DateTimeLineViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<ResultInDate>() {
        override fun areItemsTheSame(oldItem: ResultInDate, newItem: ResultInDate): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: ResultInDate, newItem: ResultInDate): Boolean =
            oldItem == newItem
    }

    inner class DateTimeLineViewHolder(private val binding: StatisticDrugTimeRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(resultInDate: ResultInDate) {
            binding.tvDate.text = getTimeStampToDateWithoutYString(resultInDate.date)
            val adapter = DrugResultScaleAdapter()
            adapter.submitList(resultInDate.results)
            binding.rvLogCheck.adapter = adapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateTimeLineViewHolder {
        return DateTimeLineViewHolder(
            StatisticDrugTimeRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DateTimeLineViewHolder, position: Int) {
        return holder.bind(getItem(position))
    }
}
