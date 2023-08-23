package com.sh.michael.simple_notepad.feature_menu.ui

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.fragment.app.Fragment
import com.sh.michael.simple_notepad.R
import com.sh.michael.simple_notepad.common.attachDrawerBorder
import com.sh.michael.simple_notepad.common.viewBinding
import com.sh.michael.simple_notepad.databinding.FragmentSideMenuBinding

class SideMenuFragment : Fragment(R.layout.fragment_side_menu) {

    private val binding by viewBinding(FragmentSideMenuBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.attachDrawerBorder(
            R.layout.menu_border,
            HANDLE_OFFSET,
            Gravity.END
        )

    }

    companion object {

        private const val HANDLE_OFFSET = 0.685f
    }
}