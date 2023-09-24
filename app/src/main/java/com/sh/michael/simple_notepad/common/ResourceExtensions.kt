package com.sh.michael.simple_notepad.common

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable

fun Int.toPx(): Int {
    return (this * Resources.getSystem().displayMetrics.density).toInt()
}

fun Int.toDp(): Int {
    return (this / Resources.getSystem().displayMetrics.density).toInt()
}

fun Int.asDrawable(context: Context): Drawable = context.resources.getDrawable(this)