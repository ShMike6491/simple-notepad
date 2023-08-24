package com.sh.michael.simple_notepad.common

import android.content.res.Resources

fun Int.toPx(): Int {
    return (this * Resources.getSystem().displayMetrics.density).toInt()
}

fun Int.toDp(): Int {
    return (this / Resources.getSystem().displayMetrics.density).toInt()
}