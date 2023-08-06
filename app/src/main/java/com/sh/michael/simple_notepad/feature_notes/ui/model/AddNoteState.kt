package com.sh.michael.simple_notepad.feature_notes.ui.model

import androidx.annotation.ColorRes
import com.sh.michael.simple_notepad.R
import com.sh.michael.simple_notepad.common.model.UiString
import com.sh.michael.simple_notepad.feature_notes.domain.model.BackgroundColor

data class AddNoteState(
    val titleValue: String = "",
    val colorSelectOptions: List<ColorOption> = emptyList(),

    val titleChangeListener: ((CharSequence?) -> Unit)? = null,
    val onSubmitAction: (() -> Unit)? = null,
    val onCloseAction: (() -> Unit)? = null,
)

data class ColorOption(
    val isColorSelected: Boolean = false,
    val colorValue: BackgroundColor = BackgroundColor.UNDEFINED,
    @ColorRes
    val backgroundColor: Int = 0,
    val onSelectAction: (() -> Unit)? = null
)

val AddNoteState.hintText: UiString get() = UiString.StringResource(R.string.note_hint_text)

val AddNoteState.selectedColor: ColorOption? get() = colorSelectOptions.find { it.isColorSelected }

val AddNoteState.backgroundColor: Int get() = selectedColor?.backgroundColor ?: R.color.white