package com.sh.michael.simple_notepad.feature_menu.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
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
            menuItemsContainer.removeAllViews()

            exitContainer.setOnClickListener { viewModel.onExitClick() }

            root.attachDrawerBorder(
                R.layout.menu_border,
                HANDLE_OFFSET,
                Gravity.END
            )
        }

        viewModel.menuState.forEach { item ->
            renderMenuItem(item)
        }

        catchLifecycleFlow(viewModel.uiEvent) { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> showSnackBar(event.state)
                is UiEvent.Navigate -> navigateTo(event.route)
                is UiEvent.Web -> navigateTo(event.route, true)
                is UiEvent.PopBackStack -> (activity as? MainActivity)?.closeDrawer()
                else -> doNothing()
            }
        }
    }

    private fun renderMenuItem(item: MenuItemState) = binding.apply {
        layoutInflater.inflate(R.layout.item_side_menu, menuItemsContainer, false)
            .apply {
                findViewById<CardView>(R.id.iconContainer)
                    .showIf(item.hasIcon)

                findViewById<ImageView>(R.id.iconImageView)
                    .setImageDrawable(item.iconRes?.asDrawable(requireContext()))

                findViewById<TextView>(R.id.titleTextView)
                    .text = item.menuTitle?.asString(requireContext())

                findViewById<ImageView>(R.id.trailingImageView)
                    .showIf(item.showTrailingIcon)

                setOnClickListener { item.onItemClickAction?.invoke() }

                menuItemsContainer.addView(this)
            }
    }

    private fun navigateTo(route: String, openWeb: Boolean = false) {
        val emailIntent = Intent(
            Intent.ACTION_SENDTO,
            Uri.parse("mailto:$CONTACT_EMAIL_ADDRESS")
        )

        val privacyIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(route)
        )

        if (route == NAVIGATE_TO_CONTACTS_ACTION) context?.startActivity(emailIntent)
        if (openWeb) context?.startActivity(privacyIntent)
    }

    companion object {

        private const val HANDLE_OFFSET = 0.685f
    }
}