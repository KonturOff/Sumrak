package com.example.sumrak.ui.inventory

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Vibrator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


class InventoryItemTouchHelper(
    private val adapter: InventoryAdapter,
    context: Context
) : ItemTouchHelper.Callback() {
    private val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        return makeMovementFlags(dragFlags, 0)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        adapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            viewHolder?.itemView?.let { itemView ->
                val animator = ObjectAnimator.ofFloat(itemView, "alpha", 1.0f, 0.5f)
                animator.duration = 200
                animator.start()

                vibrator.vibrate(50)
            }
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        viewHolder.itemView.alpha = 1.0f
        super.clearView(recyclerView, viewHolder)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // Обработка свайпа элемента, если необходимо
    }
}