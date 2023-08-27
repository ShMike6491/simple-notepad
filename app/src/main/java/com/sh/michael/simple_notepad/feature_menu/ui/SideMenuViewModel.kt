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

    val menuState = listOf(
        MenuItemState(
            "menuItem",
            StringResource(R.string.contact_me),
            R.drawable.ic_at,
            onItemClickAction = this::openEmail
        ),
        MenuItemState(
            "privacyItem",
            StringResource(R.string.privacy_policy),
            R.drawable.ic_privacy,
            onItemClickAction = this::openPrivacyWeb
        )
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

    private fun openPrivacyWeb() = viewModelScope.launch {
        val event = UiEvent.Web(PRIVACY_POLICY_LINK)
        eventChannel.send(event)
    }

    companion object {

        const val NAVIGATE_TO_CONTACTS_ACTION = "action_menu_navigate_to_contacts"
        const val CONTACT_EMAIL_ADDRESS = "inkit.notesapp@gmail.com"
        const val PRIVACY_POLICY_LINK = "https://www.google.com/"
    }
}