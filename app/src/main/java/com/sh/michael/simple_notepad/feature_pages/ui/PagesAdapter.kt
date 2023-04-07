package com.sh.michael.simple_notepad.feature_pages.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.sh.michael.simple_notepad.R
import com.sh.michael.simple_notepad.common.IdentifiableDiffCallback
import com.sh.michael.simple_notepad.feature_pages.ui.model.PageState

class PagesAdapter : ListAdapter<PageState, PageViewHolder>(IdentifiableDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PageViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_page, parent, false)
    )

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        holder.bindItem(getItem(position))
    }
}