package com.sh.michael.simple_notepad.feature_files.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.sh.michael.simple_notepad.R
import com.sh.michael.simple_notepad.common.model.UiString
import com.sh.michael.simple_notepad.common.viewBinding
import com.sh.michael.simple_notepad.databinding.FragmentFilesBinding
import com.sh.michael.simple_notepad.feature_files.ui.model.FileState

class FilesFragment : Fragment(R.layout.fragment_files) {

    private val binding by viewBinding(FragmentFilesBinding::bind)
    private val adapter by lazy { PagerAdapter(parentFragmentManager, lifecycle) }

    private val testData = listOf(
        FileState("1", UiString.DynamicString("First file")),
        FileState("2", UiString.DynamicString("A file with a very long name")),
        FileState("3", UiString.DynamicString("Third file")),
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewPager.adapter = adapter

            TabLayoutMediator(tabLayout, viewPager) {tab, position ->
                tab.text = adapter.getTitle(position).asString(requireContext())
            }.attach()

            adapter.setFiles(testData)
        }
    }
}