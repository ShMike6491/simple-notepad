package com.sh.michael.simple_notepad.feature_notes.ui.model

import androidx.annotation.ColorRes
import com.sh.michael.simple_notepad.R
import com.sh.michael.simple_notepad.common.model.UiString
import com.sh.michael.simple_notepad.feature_notes.domain.model.BackgroundColor
import com.sh.michael.simple_notepad.feature_notes.domain.model.IStickyNote

data class StickyNoteState(
    override val id: String,

    val note: IStickyNote,

    val onPrimaryAction: (() -> Unit)? = null
) : IStickyNote by note

val StickyNoteState.title: UiString get() = UiString.DynamicString(note.noteText)

@get:ColorRes
val StickyNoteState.background: Int get() = when (note.backgroundColor) {
    BackgroundColor.BLUE -> R.color.baby_blue
    BackgroundColor.PINK -> R.color.red_pink
    BackgroundColor.VIOLET -> R.color.violet
    BackgroundColor.GREEN -> R.color.light_green
    BackgroundColor.ORANGE -> R.color.red_orange
    else -> R.color.white
}