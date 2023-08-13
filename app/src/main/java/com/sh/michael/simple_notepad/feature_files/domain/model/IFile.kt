package com.sh.michael.simple_notepad.feature_files.domain.model

import com.sh.michael.simple_notepad.common.interfaces.Identifiable

interface IFile : Identifiable {

    val priority: Int
    val title: String
    val isPinned: Boolean
}