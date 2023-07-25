package com.sh.michael.simple_notepad.feature_pages.domain.model

import com.sh.michael.simple_notepad.common.interfaces.Identifiable

interface IPage : Identifiable {

    val fileId: String
    val pageText: String?
}