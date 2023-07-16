package com.sh.michael.simple_notepad.common

import android.graphics.Rect
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

fun <T> Fragment.collectLatestLifecycleFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest(collect)
        }
    }
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