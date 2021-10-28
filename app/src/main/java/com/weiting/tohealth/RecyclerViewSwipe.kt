package com.weiting.tohealth

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.view.ViewDebug
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.weiting.tohealth.homepage.*
import com.weiting.tohealth.homepage.recorddialog.DrugRecordDialog
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

abstract class RecyclerViewSwipe(val context: Context, val viewModel: HomeViewModel) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private val skipIcon = R.drawable.ic_baseline_notifications_off_24
    private val doneIcon = R.drawable.ic_baseline_done_24
    private val skipBackgroundColor = Color.parseColor("#E29578")
    private val doneBackgroundColor = Color.parseColor("#006D77")
    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

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

        val itemView = viewHolder.itemView
        val isCanceled = dX == 0f && !isCurrentlyActive

        if (isCanceled) {
            clearCanvas(
                c,
                itemView.right + dX,
                itemView.top.toFloat(),
                itemView.right.toFloat(),
                itemView.bottom.toFloat()
            )
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }

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

    private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        c?.drawRect(left, top, right, bottom, clearPaint)
    }

}