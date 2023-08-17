package com.sh.michael.simple_notepad.feature_notes.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.sh.michael.simple_notepad.databinding.ItemStickyNoteBinding
import com.sh.michael.simple_notepad.feature_notes.ui.model.StickyNoteState
import com.sh.michael.simple_notepad.feature_notes.ui.model.background
import com.sh.michael.simple_notepad.feature_notes.ui.model.title

class StickyNoteViewHolder(
    private val view: View,
) : RecyclerView.ViewHolder(view) {

    private val itemBinding by lazy { ItemStickyNoteBinding.bind(view) }

    fun bindItem(item: StickyNoteState) = itemBinding.apply {
        val color = view.context.getColor(item.background)
        cardView.setCardBackgroundColor(color)
        stickyNoteTextView.text = item.title.asString(view.context)
    }
}