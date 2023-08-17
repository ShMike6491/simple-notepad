package com.sh.michael.simple_notepad.feature_notes.domain.model

import com.sh.michael.simple_notepad.common.interfaces.Identifiable

interface IStickyNote : Identifiable {

    val backgroundColor: BackgroundColor
    val noteText: String
    val priority: Int
}

enum class BackgroundColor {
    UNDEFINED, ORANGE, PINK, BLUE, VIOLET, GREEN
}