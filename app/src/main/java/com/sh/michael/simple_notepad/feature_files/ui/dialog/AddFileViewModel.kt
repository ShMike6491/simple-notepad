package com.sh.michael.simple_notepad.feature_files.ui.dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sh.michael.simple_notepad.R
import com.sh.michael.simple_notepad.common.model.ERROR_SNACKBAR
import com.sh.michael.simple_notepad.common.model.UiEvent
import com.sh.michael.simple_notepad.common.model.UiString
import com.sh.michael.simple_notepad.common.model.UiString.*
import com.sh.michael.simple_notepad.feature_files.domain.IFilesRepository
import com.sh.michael.simple_notepad.feature_files.ui.model.AddFileState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class AddFileViewModel(
    private val repository: IFilesRepository
) : ViewModel() {

    private val initialState: AddFileState = AddFileState(
        id = UUID.randomUUID().toString(),
        helperText = StringResource(R.string.enter_file_name),
        footerText = DynamicString("0/$MAX_FILE_TITLE_LENGTH"),
        footerTextColor = R.color.granite_gray,
        titleChangeListener = this::onTextChange,
        onCloseAction = this::onClose,
        onSubmitAction = this::onSubmit
    )

    private val eventChannel = Channel<UiEvent>()
    val uiEvent = eventChannel.receiveAsFlow()

    private val pageStateFlow = MutableStateFlow(initialState)
    val pageState: StateFlow<AddFileState> = pageStateFlow

    private fun onClose() = viewModelScope.launch {
        eventChannel.send(UiEvent.PopBackStack)
    }

    private fun onTextChange(value: CharSequence?) = viewModelScope.launch {
        if (value == pageState.value.value) return@launch

        pageStateFlow.value = pageState.value.copy(value = value?.toString())

        value?.let {
            updateFooterWithTextSize(it.length)
        }
    }

    private fun updateFooterWithTextSize(size: Int) = viewModelScope.launch {
        val defaultColor = R.color.granite_gray
        val errorColor = R.color.error_red
        val isTooLong = size > MAX_FILE_TITLE_LENGTH

        pageStateFlow.value = pageState.value.copy(
            footerTextColor = if (isTooLong) errorColor else defaultColor,
            footerText = DynamicString("$size/$MAX_FILE_TITLE_LENGTH")
        )
    }

    private fun onSubmit() = viewModelScope.launch {
        if (isFormValid().not()) return@launch

        pageState.value.value?.let {
            withContext(Dispatchers.IO) {
                repository.createFile(fileName = it)
            }
            onClose()
        }
    }

    private fun isFormValid(): Boolean {
        if (pageState.value.value.isNullOrBlank()) {
            showError(DynamicString("You must provide title"))
            return false
        }

        if (pageState.value.value!!.length > MAX_FILE_TITLE_LENGTH) {
            showError(StringResource(R.string.title_is_too_long))
            return false
        }

        return true
    }

    private fun showError(message: UiString) = viewModelScope.launch {
        val successSnackbar = ERROR_SNACKBAR.copy(
            title = message
        )

        eventChannel.send(UiEvent.ShowSnackbar(successSnackbar))
    }

    companion object {

        const val MAX_FILE_TITLE_LENGTH = 50
    }
}