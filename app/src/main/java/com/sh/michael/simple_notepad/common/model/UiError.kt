package com.sh.michael.simple_notepad.common.model

import androidx.annotation.DrawableRes

/**
 * This error event state should be used in pair with [R.layout.error_layout]
 * Each page can configure error state differently.
 */
data class UiError(
    @DrawableRes val errorImage: Int? = null,
    val titleText: UiString? = null,
    val messageText: UiString? = null
) {
    val hasImage: Boolean get() = errorImage != null
}