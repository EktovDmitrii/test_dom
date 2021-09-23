package com.custom.rgs_android_dom.views.text

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.utils.TranslationHelper

class MSDTextView(context: Context, attributeSet: AttributeSet) : AppCompatTextView(context, attributeSet) {

    init {
        val attrs = context.theme.obtainStyledAttributes(attributeSet, R.styleable.MSDTextView, 0, 0)
        attrs.getString(R.styleable.MSDTextView_translationTextKey)?.let { translationTextKey ->
            //TODO Add handling translation logic here
            text = TranslationHelper.getTranslation(translationTextKey)
        }

        val isUnderlined = attrs.getBoolean(R.styleable.MSDTextView_isUnderlined, false)
        if (isUnderlined){
            paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
        }
    }

}
