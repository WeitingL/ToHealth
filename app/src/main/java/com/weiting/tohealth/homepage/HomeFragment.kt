package com.weiting.tohealth.homepage

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.weiting.tohealth.*
import com.weiting.tohealth.data.UserManager
import com.weiting.tohealth.databinding.FragmentHomeBinding
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

        // TODO Refactor 2nd
        val adapter = TodayItemAdapter()
        val swipeSet = object : RecyclerViewSwipe() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                when (direction) {
                    // Skip
                    ItemTouchHelper.LEFT -> {
                        when (viewHolder.itemViewType) {
                            VIEW_TYPE_DRUG -> {

                                viewModel.swipeToSkip(
                                    SwipeData(
                                        viewModel.itemDataMediator.value?.get(position)!!,
                                        position
                                    )
                                )

                                viewModel.itemDataMediator.value?.removeAt(position)
                                adapter.notifyItemRemoved(position)

                                // At this moment the list position has changed!
                                if (isLastInTimePoint(position, adapter)) {
                                    viewModel.removeTimeHeader(
                                        SwipeData(
                                            adapter.currentList[position - 1],
                                            position - 1
                                        )
                                    )
                                    viewModel.itemDataMediator.value?.removeAt(position - 1)
                                    adapter.notifyItemRemoved(position - 1)
                                }
                                Snackbar.make(
                                    binding.rvHomeCardView,
                                    getString(R.string.itemSkip_text),
                                    Snackbar.LENGTH_LONG
                                )
                                    .setAction(
                                        getString(R.string.itemSwipe_undo)
                                    ) {
                                        viewModel.undoSwipeToSkip()
                                        adapter.notifyItemRangeInserted(position - 1, 2)
                                    }
                                    .addCallback(object :
                                            BaseTransientBottomBar.BaseCallback<Snackbar>() {
                                            override fun onDismissed(
                                                transientBottomBar: Snackbar?,
                                                event: Int
                                            ) {
                                                super.onDismissed(transientBottomBar, event)
                                                if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                                                    viewModel.postSkipLog()
                                                }
                                            }
                                        })
                                    .show()
                            }
                            VIEW_TYPE_MEASURE -> {

                                viewModel.swipeToSkip(
                                    SwipeData(
                                        adapter.currentList[position],
                                        position
                                    )
                                )

                                viewModel.itemDataMediator.value?.removeAt(position)
                                adapter.notifyItemRemoved(position)

                                if (isLastInTimePoint(position, adapter)) {
                                    viewModel.removeTimeHeader(
                                        SwipeData(
                                            adapter.currentList[position - 1],
                                            position - 1
                                        )
                                    )
                                    viewModel.itemDataMediator.value?.removeAt(position - 1)
                                    adapter.notifyItemRemoved(position - 1)
                                }

                                Snackbar.make(
                                    binding.rvHomeCardView,
                                    getString(R.string.itemSkip_text),
                                    Snackbar.LENGTH_LONG
                                )
                                    .setAction(
                                        getString(R.string.itemSwipe_undo)
                                    ) {
                                        viewModel.undoSwipeToSkip()
                                        adapter.notifyItemRangeInserted(position - 1, 2)
                                    }
                                    .addCallback(object :
                                            BaseTransientBottomBar.BaseCallback<Snackbar>() {
                                            override fun onDismissed(
                                                transientBottomBar: Snackbar?,
                                                event: Int
                                            ) {
                                                super.onDismissed(transientBottomBar, event)
                                                if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                                                    viewModel.postSkipLog()
                                                }
                                            }
                                        }).show()
                            }
                            VIEW_TYPE_EVENT -> {

                                viewModel.swipeToSkip(
                                    SwipeData(
                                        adapter.currentList[position],
                                        position
                                    )
                                )

                                viewModel.itemDataMediator.value?.removeAt(position)
                                adapter.notifyItemRemoved(position)

                                if (isLastInTimePoint(position, adapter)) {
                                    viewModel.removeTimeHeader(
                                        SwipeData(
                                            adapter.currentList[position - 1],
                                            position - 1
                                        )
                                    )
                                    Log.i("position", position.toString())
                                    viewModel.itemDataMediator.value?.removeAt(position - 1)
                                    adapter.notifyItemRemoved(position - 1)
                                }

                                Snackbar.make(
                                    binding.rvHomeCardView,
                                    getString(R.string.itemSkip_text),
                                    Snackbar.LENGTH_LONG
                                )
                                    .setAction(
                                        getString(R.string.itemSwipe_undo)
                                    ) {
                                        viewModel.undoSwipeToSkip()
                                        adapter.notifyItemRangeInserted(position - 1, 2)
                                    }
                                    .addCallback(object :
                                            BaseTransientBottomBar.BaseCallback<Snackbar>() {
                                            override fun onDismissed(
                                                transientBottomBar: Snackbar?,
                                                event: Int
                                            ) {
                                                super.onDismissed(transientBottomBar, event)
                                                if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                                                    viewModel.postSkipLog()
                                                }
                                            }
                                        }).show()
                            }
                            VIEW_TYPE_CARE -> {

                                viewModel.swipeToSkip(
                                    SwipeData(
                                        adapter.currentList[position],
                                        position
                                    )
                                )

                                viewModel.itemDataMediator.value?.removeAt(position)
                                adapter.notifyItemRemoved(position)

                                if (isLastInTimePoint(position, adapter)) {
                                    viewModel.removeTimeHeader(
                                        SwipeData(
                                            adapter.currentList[position - 1],
                                            position - 1
                                        )
                                    )
                                    viewModel.itemDataMediator.value?.removeAt(position - 1)
                                    adapter.notifyItemRemoved(position - 1)
                                }

                                Snackbar.make(
                                    binding.rvHomeCardView,
                                    getString(R.string.itemSkip_text),
                                    Snackbar.LENGTH_LONG
                                )
                                    .setAction(
                                        getString(R.string.itemSwipe_undo)
                                    ) {
                                        viewModel.undoSwipeToSkip()
                                        adapter.notifyItemRangeInserted(position - 1, 2)
                                    }
                                    .addCallback(object :
                                            BaseTransientBottomBar.BaseCallback<Snackbar>() {
                                            override fun onDismissed(
                                                transientBottomBar: Snackbar?,
                                                event: Int
                                            ) {
                                                super.onDismissed(transientBottomBar, event)
                                                if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                                                    viewModel.postSkipLog()
                                                }
                                            }
                                        }).show()
                            }
                        }
                    }

                    // Log
                    ItemTouchHelper.RIGHT -> {
                        when (viewHolder.itemViewType) {
                            VIEW_TYPE_DRUG -> {
                                viewModel.getFinishedLog(
                                    SwipeData(
                                        adapter.currentList[position],
                                        position
                                    )
                                )

                                viewModel.itemDataMediator.value?.removeAt(position)
                                adapter.notifyItemRemoved(position)

                                if (isLastInTimePoint(position, adapter)) {
                                    viewModel.removeTimeHeaderOfFinished(
                                        SwipeData(
                                            adapter.currentList[position - 1],
                                            position - 1
                                        )
                                    )
                                    viewModel.itemDataMediator.value?.removeAt(position - 1)
                                    adapter.notifyItemRemoved(position - 1)
                                }

                                Snackbar.make(
                                    binding.rvHomeCardView,
                                    getString(R.string.itemFinished_text),
                                    Snackbar.LENGTH_LONG
                                )
                                    .setAction(
                                        getString(R.string.itemSwipe_undo)
                                    ) {
                                        viewModel.undoSwipeToLog()
                                        adapter.notifyItemRangeInserted(position - 1, 2)
                                    }
                                    .addCallback(object :
                                            BaseTransientBottomBar.BaseCallback<Snackbar>() {
                                            override fun onDismissed(
                                                transientBottomBar: Snackbar?,
                                                event: Int
                                            ) {
                                                super.onDismissed(transientBottomBar, event)
                                                if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                                                    viewModel.postFinishDrugAndActivityLog()
                                                }
                                            }
                                        }).show()
                            }
                            VIEW_TYPE_MEASURE -> {
                                findNavController().navigate(
                                    NavigationDirections.actionGlobalMeasureRecordFragment(
                                        (
                                            adapter.currentList[position] as
                                                ItemDataType.MeasureType
                                            ).measure.measureData!!,
                                        (
                                            adapter.currentList[position] as
                                                ItemDataType.MeasureType
                                            ).timeInt
                                    )
                                )
                            }
                            VIEW_TYPE_EVENT -> {

                                viewModel.getFinishedLog(
                                    SwipeData(
                                        adapter.currentList[position],
                                        position
                                    )
                                )

                                viewModel.itemDataMediator.value?.removeAt(position)
                                adapter.notifyItemRemoved(position)

                                if (isLastInTimePoint(position, adapter)) {
                                    viewModel.removeTimeHeaderOfFinished(
                                        SwipeData(
                                            adapter.currentList[position - 1],
                                            position - 1
                                        )
                                    )
                                    viewModel.itemDataMediator.value?.removeAt(position - 1)
                                    adapter.notifyItemRemoved(position - 1)
                                }

                                Snackbar.make(
                                    binding.rvHomeCardView,
                                    getString(R.string.itemFinished_text),
                                    Snackbar.LENGTH_LONG
                                )
                                    .setAction(
                                        getString(R.string.itemSwipe_undo)
                                    ) {
                                        viewModel.undoSwipeToLog()
                                        adapter.notifyItemRangeInserted(position - 1, 2)
                                    }
                                    .addCallback(object :
                                            BaseTransientBottomBar.BaseCallback<Snackbar>() {
                                            override fun onDismissed(
                                                transientBottomBar: Snackbar?,
                                                event: Int
                                            ) {
                                                super.onDismissed(transientBottomBar, event)
                                                if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                                                    viewModel.postFinishDrugAndActivityLog()
                                                }
                                            }
                                        }).show()
                            }
                            VIEW_TYPE_CARE -> {
                                findNavController().navigate(
                                    NavigationDirections.actionGlobalCareRecordFragment(
                                        (
                                            adapter.currentList[position] as
                                                ItemDataType.CareType
                                            ).care.careData!!,
                                        (
                                            adapter.currentList[position] as
                                                ItemDataType.CareType
                                            ).timeInt
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
            if (it.isNotEmpty()){
                adapter.submitList(it)
            }
        }

        viewModel.totalTask.observe(viewLifecycleOwner) {
            viewModel.taskCompleted()

            binding.progressBar.max = it
        }

        viewModel.completedTask.observe(viewLifecycleOwner) {
            viewModel.taskCompleted()

            binding.progressBar.progress = it
        }

        viewModel.allCompleted.observe(viewLifecycleOwner) {
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
            rvHomeCardView.layoutManager = WrapContentLinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rvHomeCardView.adapter = adapter
            btFastAdd.setOnClickListener {
                findNavController().navigate(NavigationDirections.actionGlobalFastAddFragment())
            }
        }

        return binding.root
    }
}

class WrapContentLinearLayoutManager : LinearLayoutManager {
    constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(
        context,
        orientation,
        reverseLayout
    )

    override fun onLayoutChildren(recycler: Recycler, state: RecyclerView.State) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: IndexOutOfBoundsException) {
//            EventData.printStackTrace()
        }
    }
}
