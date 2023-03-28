package com.sh.michael.simple_notepad.feature_notes.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.sh.michael.simple_notepad.R
import com.sh.michael.simple_notepad.common.viewBinding
import com.sh.michael.simple_notepad.databinding.FragmentStickyNotesBinding

class StickyNotesFragment : Fragment(R.layout.fragment_sticky_notes) {

    private val binding by viewBinding(FragmentStickyNotesBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // todo()
    }
}