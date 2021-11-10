package com.weiting.tohealth.homepage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.weiting.tohealth.*
import com.weiting.tohealth.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private val viewModel by viewModels<HomeViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        binding.apply {
            if (getTimeStampToTimeInt(Timestamp.now()) in 600..1000) {
                tvDailyInfoTitle.text = getString(R.string.homepage_morningtitle)
                lavHomeToday.setAnimation(R.raw.sunrise)
            } else if (getTimeStampToTimeInt(Timestamp.now()) in 1000..1400) {
                tvDailyInfoTitle.text = getString(R.string.homepage_noontitle)
                lavHomeToday.setAnimation(R.raw.sunny)
            } else if (getTimeStampToTimeInt(Timestamp.now()) in 1401..1800) {
                tvDailyInfoTitle.text = getString(R.string.homepage_afternoontitle)
                lavHomeToday.setAnimation(R.raw.sunset)
            } else if (getTimeStampToTimeInt(Timestamp.now()) in 1801..2300) {
                tvDailyInfoTitle.text = getString(R.string.homepage_nighttitle)
                lavHomeToday.setAnimation(R.raw.weather_night)
            } else {
                tvDailyInfoTitle.text = getString(R.string.homepage_sleeptitle)
                lavHomeToday.setAnimation(R.raw.sleeping)
            }
        }

        val adapter = TodayItemAdapter()
        val swipeSet = object : RecyclerViewSwipe() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.position

                when (direction) {

                    //Skip
                    ItemTouchHelper.LEFT -> {
                        when (viewHolder.itemViewType) {
                            ITEM_VIEWTYPE_DRUG -> {

                                viewModel.swipeToSkip(
                                    SwipeData(
                                        adapter.currentList[position],
                                        position
                                    )
                                )

                                viewModel.itemDataMediator.value?.removeAt(position)
                                adapter.notifyItemRemoved(position)

                                //At this moment the list position has changed!
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
                                    getString(R.string.itemskip_text),
                                    Snackbar.LENGTH_LONG
                                ).setAction(
                                    getString(R.string.itemswip_undo),
                                    View.OnClickListener {
                                        viewModel.undoSwipeToSkip()
                                        adapter.notifyItemRangeInserted(position - 1, 2)
                                    }).show()

                            }
                            ITEM_VIEWTYPE_MEASURE -> {

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
                                    getString(R.string.itemskip_text),
                                    Snackbar.LENGTH_LONG
                                ).setAction(
                                    getString(R.string.itemswip_undo),
                                    View.OnClickListener {
                                        viewModel.undoSwipeToSkip()
                                        adapter.notifyItemRangeInserted(position - 1, 2)
                                    }).show()
                            }
                            ITEM_VIEWTYPE_ACTIVITY -> {

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
                                    getString(R.string.itemskip_text),
                                    Snackbar.LENGTH_LONG
                                ).setAction(
                                    getString(R.string.itemswip_undo),
                                    View.OnClickListener {
                                        viewModel.undoSwipeToSkip()
                                        adapter.notifyItemRangeInserted(position - 1, 2)
                                    }).show()
                            }
                            ITEM_VIEWTYPE_CARE -> {

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
                                    getString(R.string.itemskip_text),
                                    Snackbar.LENGTH_LONG
                                ).setAction(
                                    getString(R.string.itemswip_undo),
                                    View.OnClickListener {
                                        viewModel.undoSwipeToSkip()
                                        adapter.notifyItemRangeInserted(position - 1, 2)
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
                                    getString(R.string.itemfinished_text),
                                    Snackbar.LENGTH_LONG
                                ).setAction(
                                    getString(R.string.itemswip_undo),
                                    View.OnClickListener {
                                        viewModel.undoSwipeToLog()
                                        adapter.notifyItemRangeInserted(position - 1, 2)
                                    }).show()

                            }
                            ITEM_VIEWTYPE_MEASURE -> {
                                findNavController().navigate(
                                    NavigationDirections.actionGlobalMeasureRecordFragment(
                                        (adapter.currentList[position] as
                                                ItemDataType.MeasureType).measure.MeasureData!!,
                                        (adapter.currentList[position] as
                                                ItemDataType.MeasureType).timeInt
                                    )
                                )
                            }
                            ITEM_VIEWTYPE_ACTIVITY -> {

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
                                    getString(R.string.itemfinished_text),
                                    Snackbar.LENGTH_LONG
                                ).setAction(
                                    getString(R.string.itemswip_undo),
                                    View.OnClickListener {
                                        viewModel.undoSwipeToLog()
                                        adapter.notifyItemRangeInserted(position - 1, 2)
                                    }).show()
                            }
                            ITEM_VIEWTYPE_CARE -> {
                                findNavController().navigate(
                                    NavigationDirections.actionGlobalCareRecordFragment(
                                        (adapter.currentList[position] as
                                                ItemDataType.CareType).care.CareData!!,
                                        (adapter.currentList[position] as
                                                ItemDataType.CareType).timeInt
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
//            Log.i("position.size", it.size.toString())
            adapter.submitList(it)
        }

        viewModel.totalTask.observe(viewLifecycleOwner) {
            viewModel.taskCompleted()
            binding.apply {
                progressBar.max = it
                tvTotal.text = it.toString()
            }
        }

        viewModel.completedTask.observe(viewLifecycleOwner) {
            viewModel.taskCompleted()

            binding.apply {
                progressBar.progress = it
                tvFinished.text = it.toString()
            }
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

        viewModel.isTheNewbie.observe(viewLifecycleOwner){
            if (it){
                binding.btFastAdd.visibility = View.GONE
                binding.tvFinishedSlogan.text = getString(R.string.newbieSlogan_homepage)
            }
        }

        binding.apply {
            rvHomeCardView.adapter = adapter
            btFastAdd.setOnClickListener {
                findNavController().navigate(NavigationDirections.actionGlobalFastAddFragment())
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.postSkipLog()
        viewModel.postFinishDrugAndActivityLog()
    }
}