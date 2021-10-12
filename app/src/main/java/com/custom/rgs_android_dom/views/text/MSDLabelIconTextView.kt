package com.custom.rgs_android_dom.views.text

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.LinearLayoutCompat
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.ViewMsdLabelIconTextViewBinding
import com.custom.rgs_android_dom.domain.translations.TranslationInteractor
import com.custom.rgs_android_dom.utils.GlideApp

class MSDLabelIconTextView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attributeSet, defStyleAttr) {


    private val binding: ViewMsdLabelIconTextViewBinding = ViewMsdLabelIconTextViewBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        val attrs = context.theme.obtainStyledAttributes(attributeSet, R.styleable.MSDLabelIconTextView, 0, 0)
        attrs.getString(R.styleable.MSDLabelIconTextView_translationLabelKey)?.let { translationLabelKey->
            //TODO Add handling translation logic here
            binding.labelTextView.text = TranslationInteractor.getTranslation(translationLabelKey)
        }

        attrs.getDrawable(R.styleable.MSDLabelIconTextView_icon)?.let {
            setIcon(it)
        }
    }

    fun setLabel(label: String){
        binding.labelTextView.text = label
    }

    fun setValue(value: String){
        binding.valueTextView.text = value
    }

    fun setIcon(image: Int){
        binding.iconImageView.setImageResource(image)
    }

    fun setIcon(image: Drawable){
        binding.iconImageView.setImageDrawable(image)
    }

    fun setIcon(image: String){
        GlideApp.with(context)
            .load(image)
            .into(binding.iconImageView)
    }


}
