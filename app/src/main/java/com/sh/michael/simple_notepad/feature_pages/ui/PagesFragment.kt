package com.sh.michael.simple_notepad.feature_pages.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.sh.michael.simple_notepad.R
import com.sh.michael.simple_notepad.common.afterTextChangedListener
import com.sh.michael.simple_notepad.common.applyOnce
import com.sh.michael.simple_notepad.common.catchLifecycleFlow
import com.sh.michael.simple_notepad.common.clickWithDebounce
import com.sh.michael.simple_notepad.common.collectLatestLifecycleFlow
import com.sh.michael.simple_notepad.common.model.UiError
import com.sh.michael.simple_notepad.common.model.UiEvent.*
import com.sh.michael.simple_notepad.common.model.hasValue
import com.sh.michael.simple_notepad.common.showAndApplyIf
import com.sh.michael.simple_notepad.common.showIf
import com.sh.michael.simple_notepad.common.showSnackBar
import com.sh.michael.simple_notepad.common.viewBinding
import com.sh.michael.simple_notepad.databinding.FragmentPagesBinding
import com.sh.michael.simple_notepad.feature_pages.ui.model.PageState
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PagesFragment : Fragment(R.layout.fragment_pages) {

    private val fileIdArg: String? by lazy { arguments?.getString(FILE_ID_ARG) }
    private val viewModel: PagesViewModel by viewModel { parametersOf(fileIdArg) }
    private val binding by viewBinding(FragmentPagesBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectLatestLifecycleFlow(viewModel.stateData) {
            binding.mainEditText.showIf(it.hasError.not())
            binding.errorContainer.root.showIf(it.hasError)
            binding.loadingBar.showIf(it.isLoading)

            if (it.error == null) renderPage(it) else renderError(it.error)
        }

        catchLifecycleFlow(viewModel.uiEvent) { event ->
            if (event is ShowSnackbar) showSnackBar(event.state)
        }
    }

    private fun renderPage(data: PageState) = binding.apply {
        data.onDeleteIconClick?.let {  callback ->
            deleteIcon.applyOnce {
                clickWithDebounce { callback.invoke() }
            }
        }

        mainEditText.apply {
            hint = data.hintText?.asString(context)
            isEnabled = data.isPageEnabled

            applyOnce {
                setText(data.bodyText?.asString(context))

                data.onTextChangeAction?.let {
                    afterTextChangedListener(it)
                }
            }
        }
    }

    private fun renderError(errorState: UiError) = binding.errorContainer.apply {
        errorImageView.showAndApplyIf(errorState.hasImage) {
            errorState.errorImage?.let { setBackgroundResource(it) }
        }

        errorTitleTextView.showAndApplyIf(errorState.titleText.hasValue(context)) {
            text = errorState.titleText?.asString(context)
        }

        errorMessageTextView.showAndApplyIf(errorState.messageText.hasValue(context)) {
            text = errorState.messageText?.asString(context)
        }
    }

    companion object {

        private const val FILE_ID_ARG = "file_id"

        fun newInstance(fileId: String? = null): PagesFragment {
            val bundle = Bundle().also {
                it.putString(FILE_ID_ARG, fileId)
            }

            return PagesFragment().also {
                it.arguments = bundle
            }
        }
    }
}