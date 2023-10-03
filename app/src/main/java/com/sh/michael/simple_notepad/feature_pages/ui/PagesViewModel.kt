package com.sh.michael.simple_notepad.feature_pages.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sh.michael.simple_notepad.R
import com.sh.michael.simple_notepad.common.doNothing
import com.sh.michael.simple_notepad.common.model.DEFAULT_SNACKBAR
import com.sh.michael.simple_notepad.common.model.ERROR_SNACKBAR
import com.sh.michael.simple_notepad.common.model.UiEvent
import com.sh.michael.simple_notepad.common.model.UiString.*
import com.sh.michael.simple_notepad.feature_pages.domain.IPagesRepository
import com.sh.michael.simple_notepad.feature_pages.domain.model.IPage
import com.sh.michael.simple_notepad.feature_pages.ui.model.PageState
import com.sh.michael.simple_notepad.feature_pages.ui.model.pageErrorState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
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
    private val errorState = initialValue.copy(
        error = pageErrorState,
        isLoading = false,
        onDeleteIconClick = this::onDeleteClick
    )

    private val eventChannel = Channel<UiEvent>()
    val uiEvent = eventChannel.receiveAsFlow()

    val stateData: StateFlow<PageState> = repository.observePageForId(fileId ?: "")
        .map { mapToPageState(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, initialValue)

    private fun onTextChanged(value: CharSequence?) = viewModelScope.launch {
        if (value == stateData.value.valueText) return@launch

        currentPageId.get()?.let {
            repository.updatePage(it, value?.toString())
        }
    }

    private fun mapToPageState(page: IPage?): PageState {
        if (page == null) {
            showErrorMessage()
            return errorState
        }

        if (currentPageId.get() != page.id) currentPageId.set(page.id)

        return initialValue.copy(
            valueText = page.pageText ?: "",
            hintText = StringResource(R.string.page_hint_text),
            isLoading = false,
            isPageEnabled = true,
            error = null,
            onTextChangeAction = this::onTextChanged,
            onDeleteIconClick = this::onDeleteClick
        )
    }

    private fun onDeleteClick() {
        viewModelScope.launch {
            val title = StringResource(R.string.are_you_sure)
            val message = StringResource(R.string.delete_confirmation_helper)
            val button = StringResource(R.string.delete_caps)
            val state = DEFAULT_SNACKBAR.copy(
                title = title,
                message = message,
                buttonText = button,
                onActionClick = { onDeleteConfirm() }
            )
            val event = UiEvent.ShowSnackbar(state)
            eventChannel.send(event)
        }
    }

    private fun onDeleteConfirm() = viewModelScope.launch {
        if (fileId == null) doNothing() else repository.deleteAllFilesData(fileId)
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