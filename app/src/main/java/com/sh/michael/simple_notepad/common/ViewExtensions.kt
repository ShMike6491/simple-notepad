package com.sh.michael.simple_notepad.common

import android.view.View

fun View?.showIf(condition: Boolean): Boolean {
    this?.visibility = if (condition) View.VISIBLE else View.GONE
    return condition
}

inline fun <T : View> T.showAndApplyIf(condition: Boolean, block: T.() -> Unit): T {
    if (this.showIf(condition)) {
        this.apply(block)
    }
    return this
}