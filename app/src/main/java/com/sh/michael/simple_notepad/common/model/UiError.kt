package com.sh.michael.simple_notepad.common.model

import androidx.annotation.DrawableRes

data class UiError(
    @DrawableRes val errorImage: Int? = null,
    val titleText: UiString? = null,
    val messageText: UiString? = null
)