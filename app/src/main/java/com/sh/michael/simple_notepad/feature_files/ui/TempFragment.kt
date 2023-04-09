package com.sh.michael.simple_notepad.feature_files.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.sh.michael.simple_notepad.R
import com.sh.michael.simple_notepad.common.viewBinding
import com.sh.michael.simple_notepad.databinding.FragmentTempBinding

class TempFragment : Fragment(R.layout.fragment_temp) {

    private val fileId: String by lazy { arguments?.getString(ARG_FILE) ?: "error loading arg" }

    private val binding by viewBinding(FragmentTempBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.temporaryTitleTextView.text = "Page for file with id: $fileId"
    }

    companion object {
        private const val ARG_FILE = "temp_fragment_file_id"

        fun newInstance(fileId: String): TempFragment = TempFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_FILE, fileId)
            }
        }
    }
}