package com.custom.rgs_android_dom.views.text

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.ViewMsdTextViewIconBinding
import com.custom.rgs_android_dom.utils.GlideApp

class MSDTextViewIcon @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {

    private val binding: ViewMsdTextViewIconBinding = ViewMsdTextViewIconBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        val attrs = context.theme.obtainStyledAttributes(attributeSet, R.styleable.MSDTextViewIcon, 0, 0)

        attrs.getString(R.styleable.MSDTextViewIcon_translationTextKey)?.let { translationTextKey ->
            //TODO Add handling translation logic here
            binding.valueTextView.text = translationTextKey
        }

        attrs.getDrawable(R.styleable.MSDTextViewIcon_icon)?.let { icon->
            setIcon(icon)
        }


    }

    fun setIcon(icon: Int){
        binding.iconImageView.setImageResource(icon)
    }

    fun setIcon(icon: Drawable){
        binding.iconImageView.setImageDrawable(icon)
    }

    fun setIcon(icon: String){
        GlideApp.with(context)
            .load(icon)
            .into(binding.iconImageView)
    }

}
