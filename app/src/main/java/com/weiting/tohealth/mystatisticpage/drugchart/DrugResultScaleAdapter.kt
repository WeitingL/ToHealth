package com.weiting.tohealth.mystatisticpage.drugchart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.weiting.tohealth.R
import com.weiting.tohealth.databinding.StasticDruglogRowBinding

class DrugResultScaleAdapter :
    ListAdapter<Map<String, String>, DrugResultScaleAdapter.DrugResultViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<Map<String, String>>() {
        override fun areItemsTheSame(oldItem: Map<String, String>, newItem: Map<String, String>): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Map<String, String>, newItem: Map<String, String>): Boolean =
            oldItem == newItem
    }

    inner class DrugResultViewHolder(private val binding: StasticDruglogRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(map: Map<String, String>) {
            when (map["result"]!!.toInt()) {
                0 -> {
                    binding.apply {
                        imLog.setImageResource(R.drawable.circle_check_green)
                        tvText.text = map["time"]
                    }
                }
                1 -> {
                    binding.apply {
                        imLog.setImageResource(R.drawable.circle_check_cross)
                        tvText.text = "跳過"
                    }
                }
                2 -> {
                    binding.apply {
                        imLog.setImageResource(R.drawable.circle_check_blue)
                        tvText.text = map["time"]
                    }
                }
                3 -> {
                    binding.apply {
                        imLog.setImageResource(R.drawable.warning)
                        tvText.text = "無紀錄"
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrugResultViewHolder {
        return DrugResultViewHolder(
            StasticDruglogRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DrugResultViewHolder, position: Int) {
        return holder.bind(getItem(position))
    }
}
