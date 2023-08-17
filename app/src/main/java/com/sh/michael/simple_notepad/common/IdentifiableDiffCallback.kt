package com.sh.michael.simple_notepad.common

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.sh.michael.simple_notepad.common.interfaces.Identifiable

class IdentifiableDiffCallback<T : Identifiable> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }
}