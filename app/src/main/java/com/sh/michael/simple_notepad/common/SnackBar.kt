package com.sh.michael.simple_notepad.common

import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.sh.michael.simple_notepad.R
import com.sh.michael.simple_notepad.common.model.SnackBarState
import com.sh.michael.simple_notepad.common.model.hasValue

const val SNACKBAR_TAG = "default snackbar"

fun Fragment.showSnackBar(state: SnackBarState) = state.apply {
    val hasIcon = icon != null
    val hasTitle = title.hasValue(context)
    val hasMessage = message.hasValue(context)
    val hasButton = buttonText.hasValue(context)

    val snackbar = Snackbar.make(this@showSnackBar.requireView(), SNACKBAR_TAG, Snackbar.LENGTH_LONG)

    val customSnackView = layoutInflater.inflate(R.layout.item_snackbar, null).apply {
        backgroundColor?.let {
            findViewById<CardView>(R.id.cardLayout)
                .setCardBackgroundColor(it.asColorStateList(requireContext()))
        }

        findViewById<ImageView>(R.id.imageView).showAndApplyIf(hasIcon) {
            icon?.let { setBackgroundResource(icon) }
            backgroundTintList = contentColor.asColorStateList(context)
        }

        findViewById<TextView>(R.id.titleTextView).showAndApplyIf(hasTitle) {
            text = title?.asString(context)
            setTextColor(contentColor.asColorStateList(context))
        }

        findViewById<TextView>(R.id.messageTextView).showAndApplyIf(hasMessage) {
            text = message?.asString(context)
            setTextColor(contentColor.asColorStateList(context))
        }

        findViewById<TextView>(R.id.actionButton).showAndApplyIf(hasButton) {
            text = buttonText?.asString(context)
            setTextColor(contentColor.asColorStateList(context))
            setOnClickListener {
                onActionClick?.invoke()
                snackbar.dismiss()
            }
        }
    }

    snackbar.view.apply {
        setBackgroundColor(Color.TRANSPARENT)
        setPadding(0, 0, 0, 0)
        (this as Snackbar.SnackbarLayout).addView(customSnackView, 0)
    }

    snackbar.show()
}