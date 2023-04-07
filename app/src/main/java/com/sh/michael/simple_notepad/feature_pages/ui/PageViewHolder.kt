package com.sh.michael.simple_notepad.feature_pages.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.sh.michael.simple_notepad.common.disableScroll
import com.sh.michael.simple_notepad.common.model.hasValue
import com.sh.michael.simple_notepad.common.showAndApplyIf
import com.sh.michael.simple_notepad.databinding.ItemPageBinding
import com.sh.michael.simple_notepad.feature_pages.ui.model.PageState

class PageViewHolder(
    private val view: View
) : RecyclerView.ViewHolder(view) {

    private val itemBinding by lazy { ItemPageBinding.bind(view) }

    fun bindItem(item: PageState) = itemBinding.apply {
        pageInputEditText.apply {
            disableScroll()
            setText(item.valueText?.asString(view.context))
            hint = item.hintText?.asString(view.context)
        }

        pageFooterTextView.showAndApplyIf(item.pageTag.hasValue(view.context)) {
            text = item.pageTag?.asString(view.context)
        }
    }
}