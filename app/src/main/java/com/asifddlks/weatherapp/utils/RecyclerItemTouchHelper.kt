package com.asifddlks.weatherapp.utils

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.asifddlks.weatherapp.ui.adapters.SavedCityAdapter


/**
 * Created by Asif Ahmed on 12/3/2024.
 * asifddlks@gmail.com
 */

class RecyclerItemTouchHelper(private var listener:RecyclerItemTouchHelperListener):
    ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {

    interface RecyclerItemTouchHelperListener{
        fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction:Int, position:Int)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        listener.onSwiped(viewHolder,direction,viewHolder.adapterPosition)
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        val foregroundView = (viewHolder as SavedCityAdapter.ViewHolder).binding.viewForeground
        ItemTouchHelper.Callback.getDefaultUIUtil().onDraw(c,recyclerView,foregroundView,dX,dY,actionState,isCurrentlyActive)
    }

    override fun onChildDrawOver(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        val foregroundView = (viewHolder as SavedCityAdapter.ViewHolder).binding.viewForeground
        ItemTouchHelper.Callback.getDefaultUIUtil().onDrawOver(c,recyclerView,foregroundView,dX,dY,actionState,isCurrentlyActive)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (viewHolder!=null){
            val foregroundView = (viewHolder as SavedCityAdapter.ViewHolder).binding.viewForeground
            ItemTouchHelper.Callback.getDefaultUIUtil().onSelected(foregroundView)
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        val foregroundView = (viewHolder as SavedCityAdapter.ViewHolder).binding.viewForeground
        ItemTouchHelper.Callback.getDefaultUIUtil().clearView(foregroundView)
    }

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }
}