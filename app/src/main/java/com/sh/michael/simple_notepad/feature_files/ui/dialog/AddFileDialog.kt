package com.sh.michael.simple_notepad.feature_files.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sh.michael.simple_notepad.R
import com.sh.michael.simple_notepad.common.addKeyboardStateListener
import com.sh.michael.simple_notepad.common.applyOnce
import com.sh.michael.simple_notepad.common.asColorStateList
import com.sh.michael.simple_notepad.common.catchLifecycleFlow
import com.sh.michael.simple_notepad.common.clickWithDebounce
import com.sh.michael.simple_notepad.common.collectLatestLifecycleFlow
import com.sh.michael.simple_notepad.common.doNothing
import com.sh.michael.simple_notepad.common.hideKeyboard
import com.sh.michael.simple_notepad.common.model.UiEvent
import com.sh.michael.simple_notepad.common.onTextChangedListener
import com.sh.michael.simple_notepad.common.showSnackBar
import com.sh.michael.simple_notepad.common.viewBinding
import com.sh.michael.simple_notepad.common.windowView
import com.sh.michael.simple_notepad.databinding.DialogAddFileBinding
import com.sh.michael.simple_notepad.feature_files.ui.model.AddFileState
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddFileDialog : BottomSheetDialogFragment(R.layout.dialog_add_file) {

    private val viewModel: AddFileViewModel by viewModel()
    private val binding by viewBinding(DialogAddFileBinding::bind)

    override fun getTheme() = R.style.BottomSheetDialogTheme

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fileNameEditText.requestFocus()

        addKeyboardStateListener(binding) { _, isOpen ->
            val dialog = dialog as? BottomSheetDialog
            val dialogState = if (isOpen) STATE_EXPANDED else STATE_COLLAPSED

            dialog?.behavior?.state = dialogState
        }

        collectLatestLifecycleFlow(viewModel.pageState) { renderPage(it) }

        catchLifecycleFlow(viewModel.uiEvent) { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    hideKeyboard()
                    showSnackBar(event.state, windowView)
                }
                is UiEvent.ShowSecondarySnackbar -> {
                    hideKeyboard()
                    showSnackBar(event.state)
                }
                is UiEvent.PopBackStack -> dialog?.dismiss()
                else -> doNothing()
            }
        }
    }

    private fun renderPage(state: AddFileState) = binding.apply {
        actionButton.clickWithDebounce {
            state.onSubmitAction?.invoke()
        }

        footerTextView.apply {
            text = state.footerText?.asString(context)
            state.footerTextColor?.let {
                setTextColor(it.asColorStateList(context))
            }
        }

        fileNameEditText.applyOnce {
            hint = state.helperText?.asString(context)
            setText(state.initialTextValue?.asString(context))
            onTextChangedListener {
                state.titleChangeListener?.invoke(it)
            }
        }

        exitImageView.applyOnce {
            setOnClickListener {
                state.onCloseAction?.invoke()
            }
        }
    }

    companion object {

        private const val DIALOG_TAG = "add_file_dialog"

        fun showDialog(manager: FragmentManager) {
            AddFileDialog().show(manager, DIALOG_TAG)
        }
    }
}