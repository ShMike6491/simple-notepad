package com.sh.michael.simple_notepad.feature_notes.ui.model

import androidx.annotation.ColorInt
import com.sh.michael.simple_notepad.common.interfaces.Identifiable
import com.sh.michael.simple_notepad.common.model.UiString

data class StickyNoteState(
    override val id: String,

    @ColorInt
    val backgroundColor: Int = -1, // change to white default color ?
    val noteText: UiString,

    val onPrimaryAction: (() -> Unit)? = null
) : Identifiable