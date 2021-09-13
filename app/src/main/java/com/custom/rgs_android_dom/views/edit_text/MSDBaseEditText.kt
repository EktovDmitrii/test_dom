package com.custom.rgs_android_dom.views.edit_text

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatEditText
import com.custom.rgs_android_dom.R

class MSDBaseEditText @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context, attributeSet, defStyleAttr) {

    init {
        val attrs = context.theme.obtainStyledAttributes(attributeSet, R.styleable.MSDBaseEditText, 0, 0)
        attrs.getString(R.styleable.MSDBaseEditText_translationHintKey)?.let { translationHintKey ->
            //TODO Add handling translation logic here
            hint = translationHintKey
        }


    }
}
