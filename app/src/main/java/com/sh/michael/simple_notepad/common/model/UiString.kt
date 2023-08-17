package com.sh.michael.simple_notepad.common.model

import android.content.Context
import androidx.annotation.StringRes

/**
 * wrapper class to handle string resources inside any viewModel
 * [DynamicString] serves to wrap any string that comes from backend
 * [StringResource] serves to pass resource id inside viewModel and extract it in fragment
 * [CombinedStringResource] is useful for the cases when you need to get a dynamic string resource
 *  and place it inside another string resource as an argument
 *
 * Any Fragment can extract the string by passing its context to [asString] method.
 * [hasValue] extension is a simple solution for checking if the wrapper class holds any string value
 */
sealed class UiString {

    fun asString(context: Context): String {
        return when(this) {
            is DynamicString -> value
            is StringResource -> context.getString(resId, *args)
            is CombinedStringResource -> {
                val arguments = args.map { it.asString(context) }
                context.getString(mainResId, *arguments.toTypedArray())
            }
        }
    }

    data class DynamicString(val value: String): UiString()

    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any
    ): UiString()

    class CombinedStringResource(
        @StringRes val mainResId: Int,
        vararg val args: UiString
    ): UiString()
}

fun UiString?.hasValue(context: Context?): Boolean {
    if (context == null) return false
    return this?.asString(context).isNullOrBlank().not()
}

/**
 * extension for ease of use in unit testing
 */
val UiString.testString: String
    get() {
        return when(this) {
            is UiString.DynamicString -> value
            is UiString.StringResource -> "$resId ${args.asList()}"
            is UiString.CombinedStringResource -> "$mainResId ${args.asList()}"
            else -> "else"
        }
    }
