package com.sh.michael.simple_notepad.feature_files.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sh.michael.simple_notepad.R
import com.sh.michael.simple_notepad.common.model.UiError
import com.sh.michael.simple_notepad.common.model.UiEvent
import com.sh.michael.simple_notepad.common.model.UiString.*
import com.sh.michael.simple_notepad.feature_files.domain.IFilesRepository
import com.sh.michael.simple_notepad.feature_files.domain.model.IFile
import com.sh.michael.simple_notepad.feature_files.ui.model.FileState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn

class FilesViewModel(
    private val repository: IFilesRepository
) : ViewModel() {

    private val eventChannel = Channel<UiEvent>()
    val uiEvent = eventChannel.receiveAsFlow()

    val stateData: StateFlow<List<FileState>> = repository.observeAllFiles()
        .map { mapToStateData(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val defaultEmptyState = UiError(
        errorImage = R.drawable.image_question,
        titleText = StringResource(R.string.nothing_found),
        messageText = StringResource(R.string.empty_files_helper)
    )

    private fun mapToStateData(files: List<IFile>): List<FileState> {
        return files.map { file ->
            FileState(
                id = file.id,
                title = DynamicString(file.title)
            )
        }
    }
}