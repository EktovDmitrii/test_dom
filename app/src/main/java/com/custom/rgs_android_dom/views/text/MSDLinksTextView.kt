package com.custom.rgs_android_dom.views.text

import android.content.Context
import android.graphics.Color
import android.text.method.LinkMovementMethod
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.custom.rgs_android_dom.R

class MSDLinksTextView(context: Context, attributeSet: AttributeSet) : AppCompatTextView(context, attributeSet) {

    init {
        movementMethod = LinkMovementMethod.getInstance()
        setOnLongClickListener { true }
        highlightColor = Color.TRANSPARENT
        linksClickable = true
        setLinkTextColor(context.getColor(R.color.primary500))
    }
}
