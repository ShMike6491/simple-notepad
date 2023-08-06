package com.sh.michael.simple_notepad.feature_notes.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import com.sh.michael.simple_notepad.R
import com.sh.michael.simple_notepad.common.collectLatestLifecycleFlow
import com.sh.michael.simple_notepad.common.showIf
import com.sh.michael.simple_notepad.common.viewBinding
import com.sh.michael.simple_notepad.databinding.FragmentStickyNotesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class StickyNotesFragment : Fragment(R.layout.fragment_sticky_notes) {

    private val viewModel: StickyNotesViewModel by viewModel()
    private val binding by viewBinding(FragmentStickyNotesBinding::bind)
    private val adapter = NotesAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.notesRecycler.adapter = adapter

        val touchHelperCallback = adapter.ItemTouchHelperCallback(viewModel::notesPositionChanged)
        val itemTouchHelper = ItemTouchHelper(touchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.notesRecycler)

        collectLatestLifecycleFlow(viewModel.stateData) {
            binding.notesRecycler.showIf(it.isNotEmpty())
            adapter.submitList(it)
        }
    }
}