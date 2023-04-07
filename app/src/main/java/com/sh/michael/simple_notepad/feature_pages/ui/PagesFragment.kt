package com.sh.michael.simple_notepad.feature_pages.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.sh.michael.simple_notepad.R
import com.sh.michael.simple_notepad.common.model.UiString
import com.sh.michael.simple_notepad.common.viewBinding
import com.sh.michael.simple_notepad.databinding.FragmentPagesBinding
import com.sh.michael.simple_notepad.feature_pages.ui.model.PageState

class PagesFragment : Fragment(R.layout.fragment_pages) {

    private val binding by viewBinding(FragmentPagesBinding::bind)
    private val adapter = PagesAdapter()

    // todo remove
    private val testData = listOf(
        PageState("1", UiString.DynamicString("just text")),
        PageState("2", UiString.DynamicString("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Nisi scelerisque eu ultrices vitae. Faucibus scelerisque eleifend donec pretium vulputate. Risus commodo viverra maecenas accumsan. Ut lectus arcu bibendum at varius vel. Urna neque viverra justo nec ultrices. Sem nulla pharetra diam sit amet nisl suscipit adipiscing bibendum. Eget velit aliquet sagittis id consectetur purus ut faucibus. Tempor orci eu lobortis elementum nibh tellus molestie nunc non. In eu mi bibendum neque egestas congue quisque egestas. Arcu non odio euismod lacinia at quis risus sed. Nisi lacus sed viverra tellus in hac habitasse platea. Elit ullamcorper dignissim cras tincidunt lobortis. Tempor orci eu lobortis elementum.\n" +
            "\n" +
            "Sagittis vitae et leo duis ut. Ullamcorper sit amet risus nullam. Massa id neque aliquam vestibulum morbi. Sed viverra tellus in hac habitasse platea. Amet nisl suscipit adipiscing bibendum. Quam id leo in vitae. Ornare arcu odio ut sem nulla pharetra diam sit amet. Elementum integer enim neque volutpat ac. Adipiscing vitae proin sagittis nisl rhoncus. Orci dapibus ultrices in iaculis.\n" +
            "\n"), pageTag = UiString.DynamicString("2 of 4")),
        PageState("3", null, UiString.DynamicString("Test Hint Text")),
        PageState("4", UiString.DynamicString("display both"), UiString.DynamicString("Test Hint Text"))
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            pagesRecycler.adapter = adapter
            adapter.submitList(testData)
        }
    }
}