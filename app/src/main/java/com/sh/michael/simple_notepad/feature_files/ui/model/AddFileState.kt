package com.sh.michael.simple_notepad.feature_files.ui.model

import androidx.annotation.ColorRes
import com.sh.michael.simple_notepad.common.interfaces.Identifiable
import com.sh.michael.simple_notepad.common.model.UiString.*
import com.sh.michael.simple_notepad.common.model.UiString

data class AddFileState(
    override val id: String,
    val value: String? = null,
    val helperText: UiString? = null,
    val footerText: UiString? = null,
    @ColorRes
    val footerTextColor: Int? = null,

    val titleChangeListener: ((CharSequence?) -> Unit)? = null,
    val onSubmitAction: (() -> Unit)? = null,
    val onCloseAction: (() -> Unit)? = null
) : Identifiable {

    val initialTextValue: UiString? get() = value?.let { DynamicString(it) }
}