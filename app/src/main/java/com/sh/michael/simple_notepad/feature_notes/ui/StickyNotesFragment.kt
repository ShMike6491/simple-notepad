package com.sh.michael.simple_notepad.feature_notes.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import com.sh.michael.simple_notepad.R
import com.sh.michael.simple_notepad.common.model.UiString
import com.sh.michael.simple_notepad.common.viewBinding
import com.sh.michael.simple_notepad.databinding.FragmentStickyNotesBinding
import com.sh.michael.simple_notepad.feature_notes.ui.model.StickyNoteState
import java.util.Collections

class StickyNotesFragment : Fragment(R.layout.fragment_sticky_notes) {

    private val binding by viewBinding(FragmentStickyNotesBinding::bind)
    private val adapter = NotesAdapter()

    private val testColors = listOf(
        R.color.red_orange,
        R.color.red_pink,
        R.color.baby_blue,
        R.color.violet,
        R.color.light_green,
    )

    // todo remove
    private var testData = listOf(1, 2, 3, 4, 5, 6)
        .map { item ->
            StickyNoteState(
                "$item",
                backgroundColor = testColors.random(),
                noteText = UiString.DynamicString("$item note"),
                onPrimaryAction = { removeItem(item) }
            )
        }

    // todo remove
    private fun itemsPositionChanged(fromPosition: Int, toPosition: Int) {
        val list = testData.toMutableList()
        Collections.swap(list, fromPosition, toPosition)
        testData = list
        adapter.submitList(list)
    }

    // todo remove
    private fun removeItem(id: Int) {
        val itemData = testData.find { it.id == "$id" } ?: return
        val updatedData = testData.toMutableList()
        updatedData.remove(itemData)
        testData = updatedData
        adapter.submitList(updatedData)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.notesRecycler.adapter = adapter
        adapter.submitList(testData)

        val touchHelperCallback = adapter.ItemTouchHelperCallback(this::itemsPositionChanged)
        val itemTouchHelper = ItemTouchHelper(touchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.notesRecycler)
    }
}