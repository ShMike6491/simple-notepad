package com.sh.michael.simple_notepad.feature_home.ui.model

import com.sh.michael.simple_notepad.common.interfaces.Identifiable

data class HomeState(
    override val id: String,
    val isExtendedState: Boolean = false,
    val onPrimaryAction: (() -> Unit)? = null,
    val onNoteAction: (() -> Unit)? = null,
    val onFileAction: (() -> Unit)? = null,
) : Identifiable