package com.sh.michael.simple_notepad.common

import android.graphics.Rect
import android.os.Build
import android.os.SystemClock
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.doOnLayout
import com.google.android.material.appbar.AppBarLayout

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

/**
 * Allows to apply block of code to view only once
 *
 * [key] parameter is required for cases when you need to set several apply blocks to the same view.
 * Each key will be used for individual use case.
 */
inline fun <T : View> T.applyOnce(key: String = "defaultKey", block: T.() -> Unit): T {
    val tagSet = (tag as? Set<*>) ?: setOf<String>()
    val tagNotSet = tagSet.contains(key).not()

    if (tagNotSet) {
        this.apply(block)
        this.tag = tagSet.plus(key)
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

fun EditText.onTextChangedListener(changeListener: (CharSequence?) -> Unit) {
    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { doNothing() }
        override fun afterTextChanged(p0: Editable?) { doNothing() }
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int)  {
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

/**
 * in coordinator layout we don't always need app bar to be scrollable.
 * this extension disables scroll
 */
fun AppBarLayout.disableScroll() {
    val params = layoutParams as? CoordinatorLayout.LayoutParams
    val callback = object : AppBarLayout.Behavior.DragCallback() {
        override fun canDrag(appBar: AppBarLayout) = false
    }

    params?.behavior = AppBarLayout.Behavior().also {
        it.setDragCallback(callback)
    }
}

/**
 * this function allows us to disable Android 10 navigation gestures
 * for a specific view.
 */
fun View.disableGlobalGestures() {
    this.apply {
        doOnLayout {
            val rects = mutableListOf<Rect>()
            rects.add(Rect(0,0,width,150.toPx()))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                systemGestureExclusionRects = rects
            }
        }
    }
}

fun AppBarLayout.addCollapseStateListener(callback: (isCollapsed: Boolean) -> Unit) {
    val listener = object : AppBarStateChangeListener() {
        override fun onStateChanged(appBarLayout: AppBarLayout?, state: State) {
            callback.invoke(state == State.COLLAPSED)
        }
    }

    addOnOffsetChangedListener(listener)
}