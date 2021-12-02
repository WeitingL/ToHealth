package com.weiting.tohealth.homepage

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.weiting.tohealth.*
import com.weiting.tohealth.data.Care
import com.weiting.tohealth.data.ItemType
import com.weiting.tohealth.data.Measure
import com.weiting.tohealth.data.UserManager
import com.weiting.tohealth.databinding.FragmentHomeBinding
import com.weiting.tohealth.homepage.homeutil.SnackBarCallBackForEvent
import com.weiting.tohealth.homepage.homeutil.SnackBarCallBackForSkip
import com.weiting.tohealth.util.RecyclerViewSwipe
import java.lang.IndexOutOfBoundsException

class HomeFragment : Fragment() {

    private val viewModel by viewModels<HomeViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        val use = Firebase.auth.currentUser
        if (use == null) {
            findNavController().navigate(NavigationDirections.actionGlobalLoginFragment())
        } else {
            UserManager.UserInfo.id = use.uid
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = TodayItemAdapter()
        val snackBarCallBackForSkip = SnackBarCallBackForSkip(viewModel)
        val snackBarCallBackForEvent = SnackBarCallBackForEvent(viewModel)

        val swipeSet = object : RecyclerViewSwipe() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.position
                val lastPosition = position - 1
                val holderItem =
                    viewModel.itemDataMediator.value?.get(position) ?: ItemDataType.Error
                val rvTodolist = binding.rvHomeCardView

                when (direction) {

                    // Skip
                    ItemTouchHelper.LEFT -> {
                        when (viewHolder.itemViewType) {
                            VIEW_TYPE_DRUG -> {

                                viewModel.swipeToSkip(SwipeData(holderItem, position))

                                viewModel.itemDataMediator.value?.removeAt(position)
                                adapter.notifyItemRemoved(position)

                                // At this moment the list position has changed!
                                if (isLastInTimePoint(position, adapter)) {
                                    val timeHolder = adapter.currentList[lastPosition]

                                    viewModel.removeTimeHeader(SwipeData(timeHolder, lastPosition))
                                    viewModel.itemDataMediator.value?.removeAt(lastPosition)
                                    adapter.notifyItemRemoved(lastPosition)
                                }

                                Snackbar.make(
                                    rvTodolist,
                                    getString(R.string.itemSkip_text),
                                    Snackbar.LENGTH_LONG
                                )
                                    .setAction(getString(R.string.itemSwipe_undo)) {
                                        viewModel.undoSwipeToSkip()
                                        adapter.notifyItemRangeInserted(lastPosition, 2)
                                    }
                                    .addCallback(snackBarCallBackForSkip)
                                    .show()
                            }
                            VIEW_TYPE_MEASURE -> {

                                viewModel.swipeToSkip(SwipeData(holderItem, position))

                                viewModel.itemDataMediator.value?.removeAt(position)
                                adapter.notifyItemRemoved(position)

                                if (isLastInTimePoint(position, adapter)) {
                                    val timeHolder = adapter.currentList[lastPosition]
                                    viewModel.removeTimeHeader(SwipeData(timeHolder, lastPosition))
                                    viewModel.itemDataMediator.value?.removeAt(lastPosition)
                                    adapter.notifyItemRemoved(lastPosition)
                                }

                                Snackbar.make(
                                    rvTodolist,
                                    getString(R.string.itemSkip_text),
                                    Snackbar.LENGTH_LONG
                                )
                                    .setAction(getString(R.string.itemSwipe_undo)) {
                                        viewModel.undoSwipeToSkip()
                                        adapter.notifyItemRangeInserted(lastPosition, 2)
                                    }
                                    .addCallback(snackBarCallBackForSkip)
                                    .show()
                            }
                            VIEW_TYPE_EVENT -> {

                                viewModel.swipeToSkip(SwipeData(holderItem, position))

                                viewModel.itemDataMediator.value?.removeAt(position)
                                adapter.notifyItemRemoved(position)

                                if (isLastInTimePoint(position, adapter)) {
                                    val timeHolder = adapter.currentList[lastPosition]
                                    viewModel.removeTimeHeader(SwipeData(timeHolder, lastPosition))
                                    viewModel.itemDataMediator.value?.removeAt(lastPosition)
                                    adapter.notifyItemRemoved(lastPosition)
                                }

                                Snackbar.make(
                                    rvTodolist,
                                    getString(R.string.itemSkip_text),
                                    Snackbar.LENGTH_LONG
                                )
                                    .setAction(getString(R.string.itemSwipe_undo)) {
                                        viewModel.undoSwipeToSkip()
                                        adapter.notifyItemRangeInserted(lastPosition, 2)
                                    }
                                    .addCallback(snackBarCallBackForSkip)
                                    .show()
                            }
                            VIEW_TYPE_CARE -> {

                                viewModel.swipeToSkip(SwipeData(holderItem, position))

                                viewModel.itemDataMediator.value?.removeAt(position)
                                adapter.notifyItemRemoved(position)

                                if (isLastInTimePoint(position, adapter)) {
                                    val timeHolder = adapter.currentList[lastPosition]
                                    viewModel.removeTimeHeader(SwipeData(timeHolder, lastPosition))
                                    viewModel.itemDataMediator.value?.removeAt(lastPosition)
                                    adapter.notifyItemRemoved(lastPosition)
                                }

                                Snackbar.make(
                                    rvTodolist,
                                    getString(R.string.itemSkip_text),
                                    Snackbar.LENGTH_LONG
                                )
                                    .setAction(getString(R.string.itemSwipe_undo)) {
                                        viewModel.undoSwipeToSkip()
                                        adapter.notifyItemRangeInserted(lastPosition, 2)
                                    }
                                    .addCallback(snackBarCallBackForSkip)
                                    .show()
                            }
                        }
                    }

                    // Log
                    ItemTouchHelper.RIGHT -> {
                        when (viewHolder.itemViewType) {
                            VIEW_TYPE_DRUG -> {

                                viewModel.swipeToFinished(SwipeData(holderItem, position))

                                viewModel.itemDataMediator.value?.removeAt(position)
                                adapter.notifyItemRemoved(position)

                                Snackbar.make(
                                    rvTodolist,
                                    getString(R.string.itemFinished_text),
                                    Snackbar.LENGTH_LONG
                                )
                                    .setAction(getString(R.string.itemSwipe_undo)) {
                                        viewModel.undoSwipeToLog(ItemType.DRUG)
                                    }.show()
                            }
                            VIEW_TYPE_MEASURE -> {
                                val measureType =
                                    (adapter.currentList[position] as ItemDataType.MeasureType)

                                findNavController().navigate(
                                    NavigationDirections.actionGlobalMeasureRecordFragment(
                                        measureType.measure.measureData ?: Measure(),
                                        measureType.timeInt
                                    )
                                )
                            }
                            VIEW_TYPE_EVENT -> {

                                viewModel.swipeToFinished(SwipeData(holderItem, position))

                                viewModel.itemDataMediator.value?.removeAt(position)
                                adapter.notifyItemRemoved(position)

                                if (isLastInTimePoint(position, adapter)) {
                                    val timeHolder = adapter.currentList[lastPosition]
                                    viewModel.removeTimeHeaderOfFinished(
                                        SwipeData(timeHolder, lastPosition)
                                    )
                                    viewModel.itemDataMediator.value?.removeAt(lastPosition)
                                    adapter.notifyItemRemoved(lastPosition)
                                }

                                Snackbar.make(
                                    rvTodolist,
                                    getString(R.string.itemFinished_text),
                                    Snackbar.LENGTH_LONG
                                )
                                    .setAction(
                                        getString(R.string.itemSwipe_undo)
                                    ) {
                                        viewModel.undoSwipeToLog(ItemType.EVENT)
                                        adapter.notifyItemRangeInserted(lastPosition, 2)
                                    }
                                    .addCallback(snackBarCallBackForEvent)
                                    .show()
                            }
                            VIEW_TYPE_CARE -> {
                                val careType =
                                    adapter.currentList[position] as ItemDataType.CareType
                                findNavController().navigate(
                                    NavigationDirections.actionGlobalCareRecordFragment(
                                        careType.care.careData ?: Care(),
                                        careType.timeInt
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

        viewModel.itemDataMediator.observe(viewLifecycleOwner) {
            viewModel.getAllTaskNumber()
            viewModel.getAllCheckedTaskCount()
            if (it.isNotEmpty()) {
                adapter.submitList(it)
            }
        }

        viewModel.isAllCompleted.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    true -> {
                        lavFinished.setAnimation(R.raw.sunny)
                        lavFinished.visibility = View.VISIBLE
                        tvFinishedSlogan.visibility = View.VISIBLE
                        rvHomeCardView.visibility = View.GONE
                    }
                    false -> {
                        lavFinished.visibility = View.GONE
                        tvFinishedSlogan.visibility = View.GONE
                        rvHomeCardView.visibility = View.VISIBLE
                    }
                }
            }
        }

        binding.apply {
            rvHomeCardView.layoutManager =
                WrapContentLinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rvHomeCardView.adapter = adapter
            btFastAdd.setOnClickListener {
                findNavController().navigate(NavigationDirections.actionGlobalFastAddFragment())
            }
        }

        return binding.root
    }
}

class WrapContentLinearLayoutManager(context: Context?, orientation: Int, reverseLayout: Boolean) :
    LinearLayoutManager(
        context,
        orientation,
        reverseLayout
    ) {

    override fun onLayoutChildren(recycler: Recycler, state: RecyclerView.State) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: IndexOutOfBoundsException) {
//            EventData.printStackTrace()
        }
    }
}
