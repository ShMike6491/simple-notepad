package com.sh.michael.simple_notepad.feature_files.ui.model

import com.sh.michael.simple_notepad.common.interfaces.Identifiable
import com.sh.michael.simple_notepad.common.model.UiString

data class FileState(
    override val id: String,
    val title: UiString
) : Identifiable