package com.sh.michael.simple_notepad.feature_menu.ui.model

import androidx.annotation.DrawableRes
import com.sh.michael.simple_notepad.common.interfaces.Identifiable
import com.sh.michael.simple_notepad.common.model.UiString

data class MenuItemState(
    override val id: String,
    val menuTitle: UiString? = null,
    @DrawableRes val iconRes: Int? = null,
    val showTrailingIcon: Boolean = true,
    val onItemClickAction: (() -> Unit)? = null
) : Identifiable {

    val hasIcon: Boolean get() = (iconRes != null)
}