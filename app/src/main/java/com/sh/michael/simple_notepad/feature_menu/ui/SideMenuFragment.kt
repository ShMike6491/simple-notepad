package com.sh.michael.simple_notepad.feature_menu.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.fragment.app.Fragment
import com.sh.michael.simple_notepad.MainActivity
import com.sh.michael.simple_notepad.R
import com.sh.michael.simple_notepad.common.asDrawable
import com.sh.michael.simple_notepad.common.attachDrawerBorder
import com.sh.michael.simple_notepad.common.catchLifecycleFlow
import com.sh.michael.simple_notepad.common.doNothing
import com.sh.michael.simple_notepad.common.model.UiEvent
import com.sh.michael.simple_notepad.common.showIf
import com.sh.michael.simple_notepad.common.showSnackBar
import com.sh.michael.simple_notepad.common.viewBinding
import com.sh.michael.simple_notepad.databinding.FragmentSideMenuBinding
import com.sh.michael.simple_notepad.feature_menu.ui.SideMenuViewModel.Companion.CONTACT_EMAIL_ADDRESS
import com.sh.michael.simple_notepad.feature_menu.ui.SideMenuViewModel.Companion.NAVIGATE_TO_CONTACTS_ACTION
import com.sh.michael.simple_notepad.feature_menu.ui.model.MenuItemState
import org.koin.androidx.viewmodel.ext.android.viewModel

class SideMenuFragment : Fragment(R.layout.fragment_side_menu) {

    private val binding by viewBinding(FragmentSideMenuBinding::bind)
    private val viewModel: SideMenuViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            renderMenuItem(viewModel.emailState)

            exitContainer.setOnClickListener { viewModel.onExitClick() }

            root.attachDrawerBorder(
                R.layout.menu_border,
                HANDLE_OFFSET,
                Gravity.END
            )
        }

        catchLifecycleFlow(viewModel.uiEvent) { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> showSnackBar(event.state)
                is UiEvent.Navigate -> navigateTo(event.route)
                is UiEvent.PopBackStack -> (activity as? MainActivity)?.closeDrawer()
                else -> doNothing()
            }
        }
    }

    private fun renderMenuItem(item: MenuItemState) = binding.emailItem.apply {
        iconContainer.showIf(item.hasIcon)
        iconImageView.setImageDrawable(item.iconRes?.asDrawable(requireContext()))
        titleTextView.text = item.menuTitle?.asString(requireContext())
        trailingImageView.showIf(item.showTrailingIcon)
        root.setOnClickListener { item.onItemClickAction?.invoke() }
    }

    private fun navigateTo(route: String) {
        val emailIntent = Intent(
            Intent.ACTION_SENDTO,
            Uri.parse("mailto:$CONTACT_EMAIL_ADDRESS")
        )

        if (route == NAVIGATE_TO_CONTACTS_ACTION) context?.startActivity(emailIntent)
    }

    companion object {

        private const val HANDLE_OFFSET = 0.685f
    }
}