package com.sh.michael.simple_notepad.feature_notes.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sh.michael.simple_notepad.R
import com.sh.michael.simple_notepad.common.addKeyboardStateListener
import com.sh.michael.simple_notepad.common.afterTextChangedListener
import com.sh.michael.simple_notepad.common.asColorStateList
import com.sh.michael.simple_notepad.common.catchLifecycleFlow
import com.sh.michael.simple_notepad.common.clickWithDebounce
import com.sh.michael.simple_notepad.common.collectLatestLifecycleFlow
import com.sh.michael.simple_notepad.common.doNothing
import com.sh.michael.simple_notepad.common.hideKeyboard
import com.sh.michael.simple_notepad.common.model.UiEvent
import com.sh.michael.simple_notepad.common.setMaxValue
import com.sh.michael.simple_notepad.common.showAndApplyIf
import com.sh.michael.simple_notepad.common.showSnackBar
import com.sh.michael.simple_notepad.common.viewBinding
import com.sh.michael.simple_notepad.databinding.DialogAddStickyNoteBinding
import com.sh.michael.simple_notepad.feature_notes.ui.dialog.AddNoteViewModel.Companion.MAX_NOTE_LENGTH
import com.sh.michael.simple_notepad.feature_notes.ui.model.AddNoteState
import com.sh.michael.simple_notepad.feature_notes.ui.model.ColorOption
import com.sh.michael.simple_notepad.feature_notes.ui.model.backgroundColor
import com.sh.michael.simple_notepad.feature_notes.ui.model.hintText
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.atomic.AtomicBoolean

class AddNoteDialog : BottomSheetDialogFragment(R.layout.dialog_add_sticky_note) {

    private val viewModel: AddNoteViewModel by viewModel()
    private val binding by viewBinding(DialogAddStickyNoteBinding::bind)
    private val hasBeenInitialized = AtomicBoolean(false)

    override fun getTheme() = R.style.BottomSheetDialogTheme

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.noteEditText.requestFocus()

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
                    showSnackBar(event.state,)
                }
                is UiEvent.PopBackStack -> dialog?.dismiss()
                else -> doNothing()
            }
        }
    }

    private fun renderPage(state: AddNoteState) = binding.apply {
        noteEditText.hint = state.hintText.asString(requireContext())
        contentLayout.backgroundTintList = state.backgroundColor.asColorStateList(requireContext())
        renderColorSelectButtons(state.colorSelectOptions)

        if (hasBeenInitialized.getAndSet(true)) return@apply

        exitImageView.setOnClickListener {
            state.onCloseAction?.invoke()
        }

        noteEditText.also {
            it.setMaxValue(MAX_NOTE_LENGTH)
            it.afterTextChangedListener { text ->
                state.titleChangeListener?.invoke(text)
            }
        }

        actionButton.clickWithDebounce(3000) {
            state.onSubmitAction?.invoke()
        }
    }

    private fun renderColorSelectButtons(items: List<ColorOption>) {
        binding.colorSelectorsLayout.showAndApplyIf(items.isNotEmpty()) {
            removeAllViews()
            items.forEach { item ->
                val layout = LayoutInflater.from(context)
                    .inflate(R.layout.item_color_check_button, this, false)

                layout.findViewById<FrameLayout>(R.id.outerLayout).apply {
                    isSelected = item.isColorSelected
                }

                layout.findViewById<FrameLayout>(R.id.innerLayout).apply {
                    backgroundTintList = item.backgroundColor.asColorStateList(requireContext())
                }

                layout.setOnClickListener { item.onSelectAction?.invoke() }

                addView(layout)
            }
        }
    }

    companion object {
        const val DIALOG_TAG = "add_note_dialog"

        fun showDialog(manager: FragmentManager) {
            AddNoteDialog().show(manager, DIALOG_TAG)
        }
    }
}