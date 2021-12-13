package com.custom.rgs_android_dom.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.custom.rgs_android_dom.R

class MSDImageView(context: Context, attributeSet: AttributeSet) : AppCompatImageView(context, attributeSet) {

    init {
        val attrs = context.theme.obtainStyledAttributes(attributeSet, R.styleable.MSDImageView, 0, 0)
        val isEnabled = attrs.getBoolean(R.styleable.MSDImageView_android_enabled, true)
        setEnabled(isEnabled)

        if (attrs.hasValue(R.styleable.MSDImageView_isActivated)){
            val isActivated = attrs.getBoolean(R.styleable.MSDImageView_isActivated, true)
            setActivated(isActivated)
        }
    }

    override fun setActivated(activated: Boolean) {
        super.setActivated(activated)
    }
}
