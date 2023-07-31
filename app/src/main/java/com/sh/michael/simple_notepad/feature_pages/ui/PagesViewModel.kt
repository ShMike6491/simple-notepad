package com.sh.michael.simple_notepad.feature_pages.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sh.michael.simple_notepad.R
import com.sh.michael.simple_notepad.common.model.ERROR_SNACKBAR
import com.sh.michael.simple_notepad.common.model.UiEvent
import com.sh.michael.simple_notepad.common.model.UiString.*
import com.sh.michael.simple_notepad.feature_pages.domain.IPagesRepository
import com.sh.michael.simple_notepad.feature_pages.domain.model.IPage
import com.sh.michael.simple_notepad.feature_pages.ui.model.PageState
import com.sh.michael.simple_notepad.feature_pages.ui.model.pageErrorState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import java.util.concurrent.atomic.AtomicReference

class PagesViewModel(
    private val fileId: String? = null,
    private val repository: IPagesRepository
) : ViewModel() {

    private val initialValue = PageState(UUID.randomUUID().toString())
    private val currentPageId = AtomicReference<String?>(null)

    private val eventChannel = Channel<UiEvent>()
    val uiEvent = eventChannel.receiveAsFlow()

    private val mutableState = MutableStateFlow(initialValue)
    val stateData: StateFlow<PageState> = mutableState
        .onSubscription { initPageState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, initialValue)

    private fun initPageState() {
        if (fileId == null) {
            showErrorState()
            showErrorMessage()
            return
        }

        viewModelScope.launch {
            repository.observePageForId(fileId)
                .collectLatest {
                    if (it == null) {
                        showErrorState()
                        showErrorMessage()
                        return@collectLatest
                    }

                    pushPageUpdate(it)
                }
        }
    }

    private fun onTextChanged(value: CharSequence?) = viewModelScope.launch {
        if (value == stateData.value.valueText) return@launch

        mutableState.value = stateData.value.copy(valueText = value.toString())

        currentPageId.get()?.let {
            repository.updatePage(it, value?.toString())
        }
    }

    private fun pushPageUpdate(page: IPage) {
        if (stateData.value.valueText == page.pageText) return
        if (currentPageId.get() != page.id) currentPageId.set(page.id)

        mutableState.value = stateData.value.copy(
            valueText = page.pageText,
            hintText = StringResource(R.string.page_hint_text),
            isLoading = false,
            isPageEnabled = true,
            onTextChangeAction = this::onTextChanged
        )
    }

    private fun showErrorState() {
        mutableState.value = initialValue.copy(
            error = pageErrorState,
            isLoading = false
        )
    }

    private fun showErrorMessage() {
        viewModelScope.launch {
            val message = StringResource(R.string.error_retreiving_page)
            val state = ERROR_SNACKBAR.copy(title = message)
            val event = UiEvent.ShowSnackbar(state)
            eventChannel.send(event)
        }
    }
}