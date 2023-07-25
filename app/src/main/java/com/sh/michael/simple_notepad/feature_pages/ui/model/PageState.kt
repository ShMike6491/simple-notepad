package com.sh.michael.simple_notepad.feature_pages.ui.model

import com.sh.michael.simple_notepad.common.interfaces.Identifiable
import com.sh.michael.simple_notepad.common.model.UiString

data class PageState(
    override val id: String,
    val valueText: String? = null,
    val hintText: UiString? = null,
    val pageTag: UiString? = null,

    val onTextChangeAction: ((sequence: CharSequence?) -> Unit)? = null
) : Identifiable {
    val bodyText: UiString? get() = valueText?.let { UiString.DynamicString(it) }
}