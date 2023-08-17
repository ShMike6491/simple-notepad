package com.sh.michael.simple_notepad.common

import android.content.Context
import android.content.res.ColorStateList

fun Int.asColorStateList() = ColorStateList.valueOf(this)

fun Int.asColorStateList(context: Context) = context.getColor(this).asColorStateList()