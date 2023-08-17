package com.sh.michael.simple_notepad.common

import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

val BottomSheetDialogFragment.windowView: View get() {
    return dialog?.window?.decorView ?: requireView()
}