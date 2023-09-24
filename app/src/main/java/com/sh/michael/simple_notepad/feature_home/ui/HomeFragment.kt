package com.sh.michael.simple_notepad.feature_home.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.sh.michael.simple_notepad.R
import com.sh.michael.simple_notepad.common.applyOnce
import com.sh.michael.simple_notepad.common.catchLifecycleFlow
import com.sh.michael.simple_notepad.common.collectLatestLifecycleFlow
import com.sh.michael.simple_notepad.common.disableScroll
import com.sh.michael.simple_notepad.common.doNothing
import com.sh.michael.simple_notepad.common.model.UiEvent
import com.sh.michael.simple_notepad.common.rotateView
import com.sh.michael.simple_notepad.common.showIf
import com.sh.michael.simple_notepad.common.showInAnimation
import com.sh.michael.simple_notepad.common.showOutAnimation
import com.sh.michael.simple_notepad.common.showSnackBar
import com.sh.michael.simple_notepad.common.viewBinding
import com.sh.michael.simple_notepad.databinding.FragmentHomeBinding
import com.sh.michael.simple_notepad.feature_files.ui.dialog.AddFileDialog
import com.sh.michael.simple_notepad.feature_home.ui.HomeViewModel.Companion.OPEN_NEW_FILE_DIALOG
import com.sh.michael.simple_notepad.feature_home.ui.HomeViewModel.Companion.OPEN_NEW_NOTE_DIALOG
import com.sh.michael.simple_notepad.feature_home.ui.model.HomeState
import com.sh.michael.simple_notepad.feature_notes.ui.dialog.AddNoteDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModel()
    private val binding by viewBinding(FragmentHomeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.appbar.disableScroll()

        collectLatestLifecycleFlow(viewModel.stateData) { home ->
            renderPage(home)
        }

        catchLifecycleFlow(viewModel.uiEvent) { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> showSnackBar(event.state)
                is UiEvent.Navigate -> handleNavigation(event.route)
                is UiEvent.PopBackStack -> activity?.onBackPressed()
                else -> doNothing()
            }
        }
    }

    private fun renderPage(state: HomeState) = binding.apply {
        dimmedBackgroundView.showIf(state.isExtendedState)

        if (state.isExtendedState.not()) {
            newNoteFab.showOutAnimation()
            newFileFab.showOutAnimation()
        } else {
            newNoteFab.showInAnimation()
            newFileFab.showInAnimation()
        }

        state.onPrimaryAction?.let { action ->
            addNewFab.applyOnce {
                setOnClickListener {
                    rotateView()
                    action()
                }
            }

            dimmedBackgroundView.apply {
                setOnClickListener {
                    addNewFab.rotateView()
                    action()
                }
            }
        }

        state.onFileAction?.let { action ->
            newFileFab.applyOnce { setOnClickListener { action() } }
        }

        state.onNoteAction?.let { action ->
            newNoteFab.applyOnce { setOnClickListener { action() } }
        }
    }

    private fun handleNavigation(route: String) = binding.apply {
        when (route) {
            OPEN_NEW_NOTE_DIALOG -> fragmentManager?.let {
                addNewFab.rotateView()
                AddNoteDialog.showDialog(it)
            }
            OPEN_NEW_FILE_DIALOG -> fragmentManager?.let {
                addNewFab.rotateView()
                AddFileDialog.showDialog(it)
            }
        }
    }
}