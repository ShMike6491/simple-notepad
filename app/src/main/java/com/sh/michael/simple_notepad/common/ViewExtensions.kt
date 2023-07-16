package com.sh.michael.simple_notepad.common

import android.os.SystemClock
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.widget.EditText

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

fun EditText.disableScroll() {
    movementMethod = null
}

fun EditText.afterTextChangedListener(changeListener: (CharSequence?) -> Unit) {
    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { doNothing() }
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { doNothing() }
        override fun afterTextChanged(p0: Editable?) {
            changeListener.invoke(p0.toString())
        }
    }

    addTextChangedListener(textWatcher)
}

fun EditText.setMaxValue(value: Int) {
    filters = arrayOf(
        InputFilter.LengthFilter(value)
    )
}

fun View.clickWithDebounce(debounceTime: Long = 600L, action: () -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0

        override fun onClick(v: View) {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
            else action()

            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}