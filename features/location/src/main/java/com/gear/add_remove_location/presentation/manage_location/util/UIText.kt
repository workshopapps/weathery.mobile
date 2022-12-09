package com.gear.add_remove_location.presentation.manage_location.util

import android.content.Context
import androidx.annotation.StringRes

/**Wrapper class for conversion of String resources
 * in classes where using context is not best practice e.g view-models
 * */
sealed class UIText {
    data class DynamicString(val value: String): UIText()
    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any
    ): UIText()

    fun asString(context: Context): String {
        return when(this){
            is DynamicString -> value
            is StringResource -> context.getString(resId, *args )
        }
    }
}