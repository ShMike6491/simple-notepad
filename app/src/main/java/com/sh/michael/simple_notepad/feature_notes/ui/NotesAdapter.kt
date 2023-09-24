package com.sh.michael.simple_notepad.feature_notes.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.sh.michael.simple_notepad.R
import com.sh.michael.simple_notepad.feature_notes.ui.model.StickyNoteState
import java.util.Collections

class NotesAdapter : RecyclerView.Adapter<StickyNoteViewHolder>() {

    private val items: MutableList<StickyNoteState> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = StickyNoteViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.item_sticky_note, parent, false)
    )

    override fun onBindViewHolder(holder: StickyNoteViewHolder, position: Int) {
        holder.bindItem(items[position])
    }

    override fun getItemCount() = items.size

    fun submitList(list: List<StickyNoteState>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    inner class DragAndDropCallback(
        private val doOnMove: ((list: List<StickyNoteState>) -> Unit)? = null
    ) : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.START.or(ItemTouchHelper.END),
        ItemTouchHelper.UP
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val startPosition = viewHolder.adapterPosition
            val endPosition = target.adapterPosition

            Collections.swap(items, startPosition, endPosition)
            notifyItemMoved(startPosition, endPosition)

            return true
        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)
            doOnMove?.invoke(items)
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            // todo refactor to swipe to reveal
            val item = items[viewHolder.adapterPosition]
            item.onPrimaryAction?.invoke()
        }
    }
}