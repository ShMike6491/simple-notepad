package com.sh.michael.simple_notepad.feature_files.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sh.michael.simple_notepad.common.model.UiString
import com.sh.michael.simple_notepad.feature_files.ui.model.FileState

class PagerAdapter(
    manager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(manager, lifecycle) {

    private var files = mutableListOf<FileState>()
    private val fileIds get() = files.map { it.hashCode().toLong() }

    override fun getItemCount(): Int = files.size

    override fun createFragment(position: Int): Fragment =  TempFragment.newInstance(files[position].id)

    override fun containsItem(itemId: Long): Boolean =  fileIds.contains(itemId)

    fun getTitle(position: Int): UiString = files[position].title

    fun getItem(position: Int): FileState = files[position]

    fun setFiles(list: List<FileState>) {
        files.clear()
        files.addAll(list)
        notifyDataSetChanged()
    }
}