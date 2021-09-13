package com.custom.rgs_android_dom.views.text

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.LinearLayoutCompat
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.ViewMsdLabelTextViewBinding
import com.custom.rgs_android_dom.utils.TranslationHelper
import com.custom.rgs_android_dom.utils.gone
import com.custom.rgs_android_dom.utils.visible

class MSDLabelTextView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attributeSet, defStyleAttr) {


    private val binding: ViewMsdLabelTextViewBinding = ViewMsdLabelTextViewBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        val attrs = context.theme.obtainStyledAttributes(attributeSet, R.styleable.MSDLabelTextView, 0, 0)
        attrs.getString(R.styleable.MSDLabelTextView_translationLabelKey)?.let { translationTextKey ->
            //TODO Add handling translation logic here
            binding.labelTextView.text = TranslationHelper.getTranslation(translationTextKey)
        }
    }

    fun setLabel(label: String){
        binding.labelTextView.text = label
    }

    fun setValue(value: String){
        binding.valueTextView.text = value
        binding.valueTextView.visible()
        binding.noValueTextView.gone()
    }

    fun setNoValue(){
        binding.valueTextView.gone()
        binding.noValueTextView.visible()
    }


}
