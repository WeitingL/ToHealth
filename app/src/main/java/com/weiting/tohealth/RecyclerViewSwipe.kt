package com.weiting.tohealth

import android.content.Context
import android.graphics.*
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.weiting.tohealth.homepage.*
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

abstract class RecyclerViewSwipe() :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {

    private val skipIcon = R.drawable.ic_baseline_notifications_off_24
    private val doneIcon = R.drawable.ic_baseline_done_24
    private val skipBackgroundColor = Color.parseColor("#8266d7")
    private val doneBackgroundColor = Color.parseColor("#e7c9ff")

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {

        //If the viewHolder is timeViewHolder, the swipe function is not support.
        if (viewHolder.itemViewType == ITEM_VIEWTYPE_TIME) return 0

        return super.getMovementFlags(recyclerView, viewHolder)
    }

    //We won't move the holder up and down: return false.
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    //Delete animation
    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

        RecyclerViewSwipeDecorator.Builder(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
            .addSwipeRightBackgroundColor(doneBackgroundColor)
            .addSwipeLeftBackgroundColor(skipBackgroundColor)
            .addSwipeRightActionIcon(doneIcon)
            .addSwipeLeftActionIcon(skipIcon)
            .create()
            .decorate()

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}