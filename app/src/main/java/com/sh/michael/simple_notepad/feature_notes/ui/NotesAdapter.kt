package com.sh.michael.simple_notepad.feature_notes.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sh.michael.simple_notepad.R
import com.sh.michael.simple_notepad.common.IdentifiableDiffCallback
import com.sh.michael.simple_notepad.feature_notes.ui.model.StickyNoteState

class NotesAdapter : ListAdapter<StickyNoteState, StickyNoteViewHolder>(IdentifiableDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = StickyNoteViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.item_sticky_note, parent, false)
    )

    override fun onBindViewHolder(holder: StickyNoteViewHolder, position: Int) {
        holder.bindItem(getItem(position))
    }

    inner class ItemTouchHelperCallback (
        private val onItemMove: (fromPosition: Int, toPosition: Int) -> Unit
    ) : ItemTouchHelper.Callback() {

        override fun isLongPressDragEnabled() = true

        override fun isItemViewSwipeEnabled() = true

        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
            val dragFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            val swipeFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            return makeMovementFlags(dragFlags, swipeFlags)
        }

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            onItemMove(viewHolder.adapterPosition, target.adapterPosition)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            // todo refactor to swipe to reveal
            val item = currentList[viewHolder.adapterPosition]
            item.onPrimaryAction?.invoke()
        }
    }
}