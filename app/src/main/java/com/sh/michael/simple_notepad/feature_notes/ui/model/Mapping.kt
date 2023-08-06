package com.sh.michael.simple_notepad.feature_notes.ui.model

import com.sh.michael.simple_notepad.R
import com.sh.michael.simple_notepad.feature_notes.domain.model.BackgroundColor

val BackgroundColor.asColorRes: Int get() = when(this) {
    BackgroundColor.ORANGE -> R.color.red_orange
    BackgroundColor.PINK -> R.color.red_pink
    BackgroundColor.BLUE -> R.color.baby_blue
    BackgroundColor.VIOLET -> R.color.violet
    BackgroundColor.GREEN -> R.color.light_green
    else -> R.color.white
}