package com.sh.michael.simple_notepad.feature_pages.ui

import androidx.lifecycle.ViewModel
import com.sh.michael.simple_notepad.common.model.UiString.DynamicString
import com.sh.michael.simple_notepad.feature_pages.ui.model.PageState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onSubscription
import java.util.UUID

class PagesViewModel(
    // todo: use this to fetch needed pages for that id
    private val fileId: String? = null
) : ViewModel() {

    // todo: fetch state from repo
    private val tempPageState = PageState(
        UUID.randomUUID().toString(),
        valueText = null,
        hintText = DynamicString("Write here..."),
        pageTag = null,
        onTextChangeAction = this::onTextChanged
    )

    private val mutableState = MutableStateFlow(tempPageState)
    val stateData: StateFlow<PageState> = mutableState

    private fun onTextChanged(value: CharSequence?) {
        if (value == stateData.value.valueText) return

        // todo: update database
        mutableState.value = stateData.value.copy(valueText = value.toString())
    }
}