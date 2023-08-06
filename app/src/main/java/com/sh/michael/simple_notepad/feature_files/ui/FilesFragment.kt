package com.sh.michael.simple_notepad.feature_files.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.sh.michael.simple_notepad.R
import com.sh.michael.simple_notepad.common.catchLifecycleFlow
import com.sh.michael.simple_notepad.common.collectLatestLifecycleFlow
import com.sh.michael.simple_notepad.common.model.UiError
import com.sh.michael.simple_notepad.common.model.UiEvent
import com.sh.michael.simple_notepad.common.model.hasValue
import com.sh.michael.simple_notepad.common.showAndApplyIf
import com.sh.michael.simple_notepad.common.showIf
import com.sh.michael.simple_notepad.common.showSnackBar
import com.sh.michael.simple_notepad.common.viewBinding
import com.sh.michael.simple_notepad.databinding.FragmentFilesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FilesFragment : Fragment(R.layout.fragment_files) {

    private val viewModel: FilesViewModel by viewModel()
    private val binding by viewBinding(FragmentFilesBinding::bind)
    private val adapter by lazy { PagerAdapter(parentFragmentManager, lifecycle) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewPager.adapter = adapter

            TabLayoutMediator(tabLayout, viewPager) {tab, position ->
                tab.text = adapter.getTitle(position).asString(requireContext())
            }.attach()

            renderEmptyState(viewModel.defaultEmptyState)
        }

        collectLatestLifecycleFlow(viewModel.stateData) { files ->
            binding.emptyStateContainer
                .root
                .showIf(files.isEmpty())

            adapter.setFiles(files)
        }

        catchLifecycleFlow(viewModel.uiEvent) { event ->
            if (event is UiEvent.ShowSnackbar) showSnackBar(event.state)
        }
    }

    private fun renderEmptyState(state: UiError) = binding.emptyStateContainer.apply {
        errorImageView.showAndApplyIf(state.hasImage) {
            state.errorImage?.let { setBackgroundResource(it) }
        }

        errorTitleTextView.showAndApplyIf(state.titleText.hasValue(context)) {
            text = state.titleText?.asString(context)
            setTextColor(resources.getColor(R.color.white))
        }

        errorMessageTextView.showAndApplyIf(state.messageText.hasValue(context)) {
            text = state.messageText?.asString(context)
            setTextColor(resources.getColor(R.color.white))
        }
    }
}