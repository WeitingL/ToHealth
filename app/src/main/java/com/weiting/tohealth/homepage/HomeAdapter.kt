package com.weiting.tohealth.homepage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.weiting.tohealth.databinding.HomeRowAddItemBinding
import com.weiting.tohealth.databinding.HomeRowDailyinfoBinding
import com.weiting.tohealth.databinding.HomeRowMygroupBinding
import com.weiting.tohealth.databinding.HomeRowTodayItemsBinding
import java.lang.ClassCastException

const val HOME_VIEWTYPE_ADDTASK = 0
const val HOME_VIEWTYPE_DAILYINFO = 1
const val HOME_VIEWTYPE_TODAYTASK = 2
const val HOME_VIEWTYPE_MYGROUP = 3

class HomeAdapter() : ListAdapter<HomePageItem, RecyclerView.ViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<HomePageItem>() {
        override fun areItemsTheSame(oldItem: HomePageItem, newItem: HomePageItem): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: HomePageItem, newItem: HomePageItem): Boolean =
            oldItem == newItem
    }

    inner class AddNewTaskViewHolder(private val binding: HomeRowAddItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    inner class DailyInfoViewHolder(private val binding: HomeRowDailyinfoBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    inner class TodayTaskViewHolder(private val binding: HomeRowTodayItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(nextTask: HomePageItem.NextTask) {
            val time = nextTask.list.first().time
            val adapter = DailyItemsAdapter()
            adapter.submitList(nextTask.list)

            binding.apply {
                tvNextTime.text = time
                rvGroupInfo.adapter = adapter
            }

        }

    }

    inner class MyGroupsNewsViewHolder(private val binding: HomeRowMygroupBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is HomePageItem.AddNewItem -> HOME_VIEWTYPE_ADDTASK
            is HomePageItem.TodayAbstract -> HOME_VIEWTYPE_DAILYINFO
            is HomePageItem.NextTask -> HOME_VIEWTYPE_TODAYTASK
            is HomePageItem.MyGroupNews -> HOME_VIEWTYPE_MYGROUP
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HOME_VIEWTYPE_ADDTASK -> AddNewTaskViewHolder(
                HomeRowAddItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            HOME_VIEWTYPE_DAILYINFO -> DailyInfoViewHolder(
                HomeRowDailyinfoBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            HOME_VIEWTYPE_TODAYTASK -> TodayTaskViewHolder(
                HomeRowTodayItemsBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            HOME_VIEWTYPE_MYGROUP -> MyGroupsNewsViewHolder(
                HomeRowMygroupBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            else -> throw ClassCastException("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AddNewTaskViewHolder -> {

            }

            is DailyInfoViewHolder -> {

            }

            is TodayTaskViewHolder -> {
                holder.bind(getItem(position) as HomePageItem.NextTask)
            }

            is MyGroupsNewsViewHolder -> {

            }

        }
    }

}