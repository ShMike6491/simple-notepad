package com.sh.michael.simple_notepad.common

import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.sh.michael.simple_notepad.R
import com.sh.michael.simple_notepad.common.model.UiString
import com.sh.michael.simple_notepad.common.model.hasValue

const val SNACKBAR_TAG = "default snackbar"

fun Fragment.showErrorSnackBar(
    title: UiString? = null,
    message: UiString? = null,
    buttonText: UiString? = null,
    onActionClick: (() -> Unit)? = null
) = showCustomSnackBar(R.color.error_red, R.color.white, R.drawable.ic_error, title, message, buttonText, onActionClick)

fun Fragment.showSuccessSnackBar(
    title: UiString? = null,
    message: UiString? = null,
    buttonText: UiString? = null,
    onActionClick: (() -> Unit)? = null
) = showCustomSnackBar(R.color.green_success, R.color.white, R.drawable.ic_success, title, message, buttonText, onActionClick)

fun Fragment.showBrandedSnackBar(
    @DrawableRes icon: Int? = null,
    title: UiString? = null,
    message: UiString? = null,
    buttonText: UiString? = null,
    onActionClick: (() -> Unit)? = null
) = showCustomSnackBar(R.color.banana_yellow, R.color.black, icon, title, message, buttonText, onActionClick)

fun Fragment.showDefaultSnackBar(
    @DrawableRes icon: Int? = null,
    title: UiString? = null,
    message: UiString? = null,
    buttonText: UiString? = null,
    onActionClick: (() -> Unit)? = null
) = showCustomSnackBar(R.color.blue_netrual, R.color.white, icon, title, message, buttonText, onActionClick)

fun Fragment.showCustomSnackBar(
    @ColorRes backgroundColor: Int? = null,
    @ColorRes contentColor: Int = R.color.white,
    @DrawableRes icon: Int? = null,
    title: UiString? = null,
    message: UiString? = null,
    buttonText: UiString? = null,
    onActionClick: (() -> Unit)? = null
) {
    val hasIcon = icon != null
    val hasTitle = title.hasValue(context)
    val hasMessage = message.hasValue(context)
    val hasButton = buttonText.hasValue(context)

    val snackbar = Snackbar.make(this.requireView(), SNACKBAR_TAG, Snackbar.LENGTH_LONG)

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