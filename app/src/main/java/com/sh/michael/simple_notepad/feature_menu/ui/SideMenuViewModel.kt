package com.sh.michael.simple_notepad.feature_menu.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sh.michael.simple_notepad.R
import com.sh.michael.simple_notepad.common.model.UiEvent
import com.sh.michael.simple_notepad.common.model.UiString.*
import com.sh.michael.simple_notepad.feature_menu.ui.model.MenuItemState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SideMenuViewModel : ViewModel() {

    private val eventChannel = Channel<UiEvent>()
    val uiEvent = eventChannel.receiveAsFlow()

    val emailState = MenuItemState(
        "menuItem",
        StringResource(R.string.contact_me),
        R.drawable.ic_at,
        onItemClickAction = this::openEmail
    )

    fun onExitClick() {
        viewModelScope.launch {
            eventChannel.send(UiEvent.PopBackStack)
        }
    }

    private fun openEmail() = viewModelScope.launch {
        val event = UiEvent.Navigate(NAVIGATE_TO_CONTACTS_ACTION)
        eventChannel.send(event)
    }

    companion object {

        const val NAVIGATE_TO_CONTACTS_ACTION = "action_menu_navigate_to_contacts"
        const val CONTACT_EMAIL_ADDRESS = "inkit.notesapp@gmail.com"
    }
}