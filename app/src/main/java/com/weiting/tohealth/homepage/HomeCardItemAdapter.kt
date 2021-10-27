package com.weiting.tohealth.homepage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.weiting.tohealth.databinding.*
import java.lang.ClassCastException

const val HOME_VIEWTYPE_ADDTASK = 0
const val HOME_VIEWTYPE_DAILYINFO = 1
const val HOME_VIEWTYPE_TODAYTASK = 2

class HomeAdapter(val onClickListener: OnclickListener, val onclickListenerItem: OnclickListenerItem) :
    ListAdapter<HomePageItem, RecyclerView.ViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<HomePageItem>() {
        override fun areItemsTheSame(oldItem: HomePageItem, newItem: HomePageItem): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: HomePageItem, newItem: HomePageItem): Boolean =
            oldItem == newItem
    }

    inner class FastAddTaskViewHolder(private val binding: HomeRowFastaddBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    inner class TodayViewHolder(private val binding: HomeRowTodayBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    inner class NextTaskViewHolder(private val binding: HomeRowNexttaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(nextTask: HomePageItem.NextTask) {
            val adapter = TodayItemAdapter(TodayItemAdapter.OnclickListener {
                onclickListenerItem.onClick(it)
            })
            adapter.submitList(nextTask.list)

            binding.apply {
                rvGroupInfo.adapter = adapter
            }
        }
    }

    inner class MyGroupsNewsViewHolder(private val binding: HomeRowMygroupBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is HomePageItem.TodayAbstract -> HOME_VIEWTYPE_DAILYINFO
            is HomePageItem.NextTask -> HOME_VIEWTYPE_TODAYTASK
            is HomePageItem.AddNewItem -> HOME_VIEWTYPE_ADDTASK
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HOME_VIEWTYPE_ADDTASK -> FastAddTaskViewHolder(
                HomeRowFastaddBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            HOME_VIEWTYPE_DAILYINFO -> TodayViewHolder(
                HomeRowTodayBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            HOME_VIEWTYPE_TODAYTASK -> NextTaskViewHolder(
                HomeRowNexttaskBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            else -> throw ClassCastException("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FastAddTaskViewHolder -> {
                holder.itemView.setOnClickListener {
                    onClickListener.onClick(getItem(position))
                }
            }

            is TodayViewHolder -> {
            }

            is NextTaskViewHolder -> {
                holder.bind(getItem(position) as HomePageItem.NextTask)
            }
        }
    }

    class OnclickListener(val clickListener: (homePageItem: HomePageItem) -> Unit) {
        fun onClick(homePageItem: HomePageItem) = clickListener(homePageItem)
    }

    class OnclickListenerItem(val clickListener: (itemDataType: ItemDataType) -> Unit) {
        fun onClick(itemDataType: ItemDataType) = clickListener(itemDataType)
    }

}

