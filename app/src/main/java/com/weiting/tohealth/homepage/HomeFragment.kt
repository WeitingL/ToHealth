package com.weiting.tohealth.homepage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.weiting.tohealth.NavigationDirections
import com.weiting.tohealth.PublicApplication
import com.weiting.tohealth.RecyclerViewSwipe
import com.weiting.tohealth.data.*
import com.weiting.tohealth.databinding.FragmentHomeBinding
import com.weiting.tohealth.factory.RecordViewModelFactory
import com.weiting.tohealth.getVmFactory
import com.weiting.tohealth.itemeditpage.TimeSetAdapter

class HomeFragment : Fragment() {

    private val viewModel by viewModels<HomeViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        val factory = RecordViewModelFactory(PublicApplication.application.firebaseDataRepository)
        val recordViewModel = ViewModelProvider(this, factory).get(RecordViewModel::class.java)
        val adapter = TodayItemAdapter()
        val swipeSet = object : RecyclerViewSwipe() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when (direction) {

                    //Skip
                    ItemTouchHelper.LEFT -> {
                        when (viewHolder.itemViewType) {

                            ITEM_VIEWTYPE_DRUG -> {
                                viewModel.swipeToSkip(
                                    SwipeData(
                                        adapter.currentList[viewHolder.bindingAdapterPosition],
                                        viewHolder.bindingAdapterPosition
                                    )
                                )

                                viewModel.itemDataMediator.value?.removeAt(viewHolder.bindingAdapterPosition)
                                adapter.notifyItemRemoved(viewHolder.bindingAdapterPosition)

                                Snackbar.make(binding.rvHomeCardView, "跳過", Snackbar.LENGTH_LONG)
                                    .setAction("回復", View.OnClickListener {
                                        viewModel.undoSwipeToSkip()
                                        adapter.notifyItemInserted(viewHolder.bindingAdapterPosition)
                                    }).show()

                            }
                            ITEM_VIEWTYPE_MEASURE -> {
                                viewModel.swipeToSkip(
                                    SwipeData(
                                        adapter.currentList[viewHolder.bindingAdapterPosition],
                                        viewHolder.bindingAdapterPosition
                                    )
                                )

                                viewModel.itemDataMediator.value?.removeAt(viewHolder.bindingAdapterPosition)
                                adapter.notifyItemRemoved(viewHolder.bindingAdapterPosition)

                                Snackbar.make(binding.rvHomeCardView, "跳過", Snackbar.LENGTH_LONG)
                                    .setAction("回復", View.OnClickListener {
                                        viewModel.undoSwipeToSkip()
                                        adapter.notifyItemInserted(viewHolder.bindingAdapterPosition)
                                    }).show()
                            }
                            ITEM_VIEWTYPE_ACTIVITY -> {
                                viewModel.swipeToSkip(
                                    SwipeData(
                                        adapter.currentList[viewHolder.bindingAdapterPosition],
                                        viewHolder.bindingAdapterPosition
                                    )
                                )

                                viewModel.itemDataMediator.value?.removeAt(viewHolder.bindingAdapterPosition)
                                adapter.notifyItemRemoved(viewHolder.bindingAdapterPosition)

                                Snackbar.make(binding.rvHomeCardView, "跳過", Snackbar.LENGTH_LONG)
                                    .setAction("回復", View.OnClickListener {
                                        viewModel.undoSwipeToSkip()
                                        adapter.notifyItemInserted(viewHolder.bindingAdapterPosition)
                                    }).show()
                            }
                            ITEM_VIEWTYPE_CARE -> {
                                viewModel.swipeToSkip(
                                    SwipeData(
                                        adapter.currentList[viewHolder.bindingAdapterPosition],
                                        viewHolder.bindingAdapterPosition
                                    )
                                )

                                viewModel.itemDataMediator.value?.removeAt(viewHolder.bindingAdapterPosition)
                                adapter.notifyItemRemoved(viewHolder.bindingAdapterPosition)

                                Snackbar.make(binding.rvHomeCardView, "跳過", Snackbar.LENGTH_LONG)
                                    .setAction("回復", View.OnClickListener {
                                        viewModel.undoSwipeToSkip()
                                        adapter.notifyItemInserted(viewHolder.bindingAdapterPosition)
                                    }).show()
                            }
                        }
                    }

                    //Log
                    ItemTouchHelper.RIGHT -> {
                        when (viewHolder.itemViewType) {
                            ITEM_VIEWTYPE_DRUG -> {
                                viewModel.getFinishedLog(
                                    SwipeData(
                                        adapter.currentList[viewHolder.bindingAdapterPosition],
                                        viewHolder.bindingAdapterPosition
                                    )
                                )

                                viewModel.itemDataMediator.value?.removeAt(viewHolder.bindingAdapterPosition)
                                adapter.notifyItemRemoved(viewHolder.bindingAdapterPosition)

                                Snackbar.make(binding.rvHomeCardView, "完成", Snackbar.LENGTH_LONG)
                                    .setAction("回復", View.OnClickListener {
                                        viewModel.undoSwipeToLog()
                                        adapter.notifyItemInserted(viewHolder.bindingAdapterPosition)
                                    }).show()

                            }

                            ITEM_VIEWTYPE_MEASURE -> {
                                findNavController().navigate(
                                    NavigationDirections.actionGlobalMeasureRecordFragment(
                                        (adapter.currentList[viewHolder.bindingAdapterPosition] as ItemDataType.MeasureType).measure.MeasureData!!,
                                        viewHolder.bindingAdapterPosition
                                    )
                                )
                            }

                            ITEM_VIEWTYPE_ACTIVITY -> {
                                viewModel.getFinishedLog(
                                    SwipeData(
                                        adapter.currentList[viewHolder.bindingAdapterPosition],
                                        viewHolder.bindingAdapterPosition
                                    )
                                )

                                viewModel.itemDataMediator.value?.removeAt(viewHolder.bindingAdapterPosition)
                                adapter.notifyItemRemoved(viewHolder.bindingAdapterPosition)

                                Snackbar.make(binding.rvHomeCardView, "完成", Snackbar.LENGTH_LONG)
                                    .setAction("回復", View.OnClickListener {
                                        viewModel.undoSwipeToLog()
                                        adapter.notifyItemInserted(viewHolder.bindingAdapterPosition)
                                    }).show()
                            }

                            ITEM_VIEWTYPE_CARE -> {
                                findNavController().navigate(
                                    NavigationDirections.actionGlobalCareRecordFragment(
                                        (adapter.currentList[viewHolder.bindingAdapterPosition] as ItemDataType.CareType).care.CareData!!,
                                        viewHolder.bindingAdapterPosition
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        val touchHelper = ItemTouchHelper(swipeSet)
        touchHelper.attachToRecyclerView(binding.rvHomeCardView)

        //Get data from firebase
        viewModel.itemDataMediator.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.apply {
            rvHomeCardView.adapter = adapter
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        viewModel.postSkipLog()
//        viewModel.postFinishDrugAndActivityLog()
    }
}