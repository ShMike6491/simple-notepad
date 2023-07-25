package com.sh.michael.simple_notepad.feature_pages.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.sh.michael.simple_notepad.R
import com.sh.michael.simple_notepad.common.afterTextChangedListener
import com.sh.michael.simple_notepad.common.applyOnce
import com.sh.michael.simple_notepad.common.collectLatestLifecycleFlow
import com.sh.michael.simple_notepad.common.viewBinding
import com.sh.michael.simple_notepad.databinding.FragmentPagesBinding
import com.sh.michael.simple_notepad.feature_pages.ui.model.PageState
import org.koin.androidx.viewmodel.ext.android.viewModel

class PagesFragment : Fragment(R.layout.fragment_pages) {

    private val viewModel: PagesViewModel by viewModel()
    private val binding by viewBinding(FragmentPagesBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectLatestLifecycleFlow(viewModel.stateData) {
            renderPage(it)
        }
    }

    private fun renderPage(data: PageState) = binding.mainEditText.apply {
        hint = data.hintText?.asString(context)

        applyOnce {
            setText(data.bodyText?.asString(context))

            data.onTextChangeAction?.let {
                afterTextChangedListener(it)
            }
        }
    }
}