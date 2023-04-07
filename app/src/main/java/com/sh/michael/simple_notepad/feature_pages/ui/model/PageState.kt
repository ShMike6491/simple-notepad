package com.sh.michael.simple_notepad.feature_pages.ui.model

import com.sh.michael.simple_notepad.common.interfaces.Identifiable
import com.sh.michael.simple_notepad.common.model.UiString

data class PageState(
    override val id: String,
    val valueText: UiString? = null,
    val hintText: UiString? = null,
    val pageTag: UiString? = null,

    // todo add text listener to track changes
) : Identifiable