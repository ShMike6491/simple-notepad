package com.sh.michael.simple_notepad.feature_files.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sh.michael.simple_notepad.R
import com.sh.michael.simple_notepad.common.model.UiError
import com.sh.michael.simple_notepad.common.model.UiEvent
import com.sh.michael.simple_notepad.common.model.UiString
import com.sh.michael.simple_notepad.common.model.UiString.*
import com.sh.michael.simple_notepad.feature_files.domain.IFilesRepository
import com.sh.michael.simple_notepad.feature_files.domain.model.IFile
import com.sh.michael.simple_notepad.feature_files.ui.model.FileState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn

class FilesViewModel(
    private val repository: IFilesRepository
) : ViewModel() {

    // todo: remove and switch to repo data
    private val actualFiles = repository.observeAllFiles()
        .map { mapToStateData(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val emptyData = emptyList<FileState>()
    private val testData = listOf(
        FileState("1", DynamicString("First file")),
        // fixme: learn how to prevent long title text size to change
        FileState("2", DynamicString("A file with a very long name")),
        FileState("3", DynamicString("Third file")),
    )

    private val eventChannel = Channel<UiEvent>()
    val uiEvent = eventChannel.receiveAsFlow()

    val stateData: StateFlow<List<FileState>> = MutableStateFlow(testData) //change here

    val defaultEmptyState = UiError(
        errorImage = R.drawable.image_question,
        titleText = DynamicString("Nothing found..."),
        messageText = DynamicString("Looks like there are no files found in your app.")
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