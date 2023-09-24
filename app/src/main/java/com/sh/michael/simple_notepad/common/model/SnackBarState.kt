package com.sh.michael.simple_notepad.common.model

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.sh.michael.simple_notepad.R

val ERROR_SNACKBAR = SnackBarState(R.color.error_red, R.color.white, R.drawable.ic_error, null, null, null, null)
val SUCCESS_SNACKBAR = SnackBarState(R.color.green_success, R.color.white, R.drawable.ic_success, null, null, null, null)
val BRANDED_SNACKBAR = SnackBarState(R.color.banana_yellow, R.color.black, null, null, null, null, null)
val DEFAULT_SNACKBAR = SnackBarState(R.color.blue_netrual, R.color.white, null, null, null, null, null)

data class SnackBarState(
    @ColorRes
    val backgroundColor: Int? = null,
    @ColorRes
    val contentColor: Int = R.color.white,
    @DrawableRes
    val icon: Int? = null,
    val title: UiString? = null,
    val message: UiString? = null,
    val buttonText: UiString? = null,
    val onActionClick: (() -> Unit)? = null
)