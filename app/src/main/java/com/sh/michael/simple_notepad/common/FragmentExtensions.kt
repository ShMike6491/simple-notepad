package com.sh.michael.simple_notepad.common

import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.Rect
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

fun <T> Fragment.collectLatestLifecycleFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
    val isCollecting = MutableSharedFlow<Boolean>(replay = 1)

    lifecycleScope.launch {
        flow.onStart { isCollecting.emit(true) }
            .collectLatest { value ->
                // Don't collect if another collection is already ongoing
                if (isCollecting.firstOrNull() == true) {
                    collect(value)
                }
            }
    }
}

fun <T> Fragment.collectLifecycleFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect(collect)
        }
    }
}

fun <T> Fragment.catchLifecycleFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
    flow.catch {}
        .onEach(collect)
        .launchIn(lifecycleScope)
}

fun Fragment.addKeyboardStateListener(binding: ViewBinding, callback: (height: Int, isOpen: Boolean) -> Unit) {
    val rootView = binding.root

    rootView.viewTreeObserver.addOnGlobalLayoutListener {
        val rect = Rect()
        rootView.getWindowVisibleDisplayFrame(rect)
        val screenHeight = rootView.rootView.height
        val keypadHeight = screenHeight - rect.bottom

        // 0.15 ratio is perhaps enough to determine keypad height.
        val isStateOpen = keypadHeight > screenHeight * 0.15

        callback.invoke(keypadHeight, isStateOpen)
    }
}

fun Fragment.hideKeyboard() {
    val imm = context?.getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(view?.windowToken, 0)
}

fun Fragment.showKeyboard() {
    val imm = context?.getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.showSoftInputFromInputMethod(view?.windowToken, 0)
}