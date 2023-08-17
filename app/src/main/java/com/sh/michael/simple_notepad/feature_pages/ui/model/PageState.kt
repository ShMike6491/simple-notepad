package com.sh.michael.simple_notepad.feature_pages.ui.model

import com.sh.michael.simple_notepad.R
import com.sh.michael.simple_notepad.common.interfaces.Identifiable
import com.sh.michael.simple_notepad.common.model.UiError
import com.sh.michael.simple_notepad.common.model.UiString
import com.sh.michael.simple_notepad.common.model.UiString.*

data class PageState(
    override val id: String,
    val valueText: String? = null,
    val hintText: UiString? = null,
    val pageTag: UiString? = null,

    val error: UiError? = null,
    val isLoading: Boolean = true,
    val isPageEnabled: Boolean = false,
    val onTextChangeAction: ((sequence: CharSequence?) -> Unit)? = null,
    val onDeleteIconClick: (() -> Unit)? = null
) : Identifiable {
    val bodyText: UiString? get() = valueText?.let { DynamicString(it) }
    val hasError: Boolean get() = error != null
}

val pageErrorState = UiError(
    errorImage = R.drawable.image_error_robot,
    titleText = StringResource(R.string.something_went_wrong),
    messageText = StringResource(R.string.page_error_helper)
)