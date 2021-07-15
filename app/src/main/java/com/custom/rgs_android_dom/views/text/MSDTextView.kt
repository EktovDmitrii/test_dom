package com.custom.rgs_android_dom.views.text

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.custom.rgs_android_dom.R

class MSDTextView(context: Context, attributeSet: AttributeSet) : AppCompatTextView(context, attributeSet) {

    init {
        val attrs = context.theme.obtainStyledAttributes(attributeSet, R.styleable.MSDTextView, 0, 0)
        attrs.getString(R.styleable.MSDTextView_translationTextKey)?.let { translationTextKey ->
            //TODO Add handling translation logic here
        }
    }
}
