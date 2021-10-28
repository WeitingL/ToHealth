package com.weiting.tohealth.homepage

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.weiting.tohealth.PublicApplication
import com.weiting.tohealth.RecyclerViewSwipe
import com.weiting.tohealth.databinding.*
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import java.lang.ClassCastException

const val HOME_VIEWTYPE_ADDTASK = 0
const val HOME_VIEWTYPE_DAILYINFO = 1
const val HOME_VIEWTYPE_TODAYTASK = 2

class HomeAdapter(
    val onClickListener: OnclickListener,
    val onclickListenerItem: OnclickListenerItem,
    val viewModel: HomeViewModel
) :
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

            val context = PublicApplication.application.applicationContext
            val swipeSet =
                object : RecyclerViewSwipe(context, viewModel) {
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        when (direction) {

                            //Skip
                            ItemTouchHelper.LEFT -> {
                                when (viewHolder.itemViewType) {
                                    ITEM_VIEWTYPE_DRUG -> {
                                        //Get position to find out the data from list!!
                                        viewModel.swipeToSkip(nextTask.list[viewHolder.bindingAdapterPosition])
                                    }
                                    ITEM_VIEWTYPE_MEASURE -> {
                                        viewModel.swipeToSkip(nextTask.list[viewHolder.bindingAdapterPosition])
                                    }
                                    ITEM_VIEWTYPE_ACTIVITY -> {
                                        viewModel.swipeToSkip(nextTask.list[viewHolder.bindingAdapterPosition])
                                    }
                                    ITEM_VIEWTYPE_CARE -> {
                                        viewModel.swipeToSkip(nextTask.list[viewHolder.bindingAdapterPosition])
                                    }
                                }
                            }

                            //Entry data
                            ItemTouchHelper.RIGHT -> {
                                when (viewHolder.itemViewType) {
                                    ITEM_VIEWTYPE_DRUG -> {
                                        viewModel.swipeToNavigate(nextTask.list[viewHolder.bindingAdapterPosition])
                                    }
                                    ITEM_VIEWTYPE_MEASURE -> {
                                        viewModel.swipeToNavigate(nextTask.list[viewHolder.bindingAdapterPosition])
                                    }
                                    ITEM_VIEWTYPE_ACTIVITY -> {
                                        viewModel.swipeToNavigate(nextTask.list[viewHolder.bindingAdapterPosition])
                                    }
                                    ITEM_VIEWTYPE_CARE -> {
                                        viewModel.swipeToNavigate(nextTask.list[viewHolder.bindingAdapterPosition])
                                    }
                                }
                            }
                        }
                    }
                }

            val touchHelper = ItemTouchHelper(swipeSet)
            touchHelper.attachToRecyclerView(binding.rvGroupInfo)

            val adapter = TodayItemAdapter(TodayItemAdapter.OnclickListener {
                onclickListenerItem.onClick(it)
            }, viewModel)
            adapter.submitList(nextTask.list)

            binding.apply {
                rvGroupInfo.adapter = adapter
            }
        }
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

