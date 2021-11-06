package com.weiting.tohealth.mystatisticpage.activitychart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.weiting.tohealth.R
import com.weiting.tohealth.databinding.StasticDruglogRowBinding
import com.weiting.tohealth.mystatisticpage.drugchart.DrugResultScaleAdapter

class ActivityResultScaleAdapter:
    ListAdapter<Int, ActivityResultScaleAdapter.ActivityResultViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean =
            oldItem == newItem

    }


    inner class ActivityResultViewHolder(private val binding: StasticDruglogRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(int: Int) {
            when(int){
                0 -> binding.imLog.setImageResource(R.drawable.success)
                1 -> binding.imLog.setImageResource(R.drawable.error)
                2 -> binding.imLog.setImageResource(R.drawable.warning)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityResultViewHolder {
        return ActivityResultViewHolder(
            StasticDruglogRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ActivityResultViewHolder, position: Int) {
        return holder.bind(getItem(position))
    }

}