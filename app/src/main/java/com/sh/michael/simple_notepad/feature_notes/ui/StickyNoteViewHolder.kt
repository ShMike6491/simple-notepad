package com.sh.michael.simple_notepad.feature_notes.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.sh.michael.simple_notepad.databinding.ItemStickyNoteBinding
import com.sh.michael.simple_notepad.feature_notes.ui.model.StickyNoteState

class StickyNoteViewHolder(
    private val view: View,
) : RecyclerView.ViewHolder(view) {

    private val itemBinding by lazy { ItemStickyNoteBinding.bind(view) }

    fun bindItem(item: StickyNoteState) = itemBinding.apply {
        // todo refactor to normal background setter
        val colorRes = item.backgroundColor.toInt()
        cardView.setCardBackgroundColor(view.context.getColor(colorRes))
        stickyNoteTextView.text = item.noteText.asString(view.context)
    }
}