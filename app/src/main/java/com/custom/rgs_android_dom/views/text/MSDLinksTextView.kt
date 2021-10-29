package com.custom.rgs_android_dom.views.text

import android.content.Context
import android.graphics.Color
import android.text.method.LinkMovementMethod
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.domain.translations.TranslationInteractor

class MSDLinksTextView(context: Context, attributeSet: AttributeSet) : AppCompatTextView(context, attributeSet) {

    init {
        movementMethod = LinkMovementMethod.getInstance()
        setOnLongClickListener { true }
        highlightColor = Color.TRANSPARENT
        linksClickable = true
        setLinkTextColor(context.getColor(R.color.primary500))

        val attrs = context.theme.obtainStyledAttributes(attributeSet, R.styleable.MSDLinksTextView, 0, 0)
        attrs.getString(R.styleable.MSDLinksTextView_translationTextKey)?.let { translationTextKey ->
            //TODO Add handling translation logic here
            text = TranslationInteractor.getTranslation(translationTextKey)
        }
    }
}
