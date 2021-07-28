package com.custom.rgs_android_dom.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.RelativeLayout
import androidx.core.widget.addTextChangedListener
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.ViewMsdGenderSelectorBinding
import com.custom.rgs_android_dom.databinding.ViewMsdLabelIconEditTextBinding
import com.custom.rgs_android_dom.utils.GlideApp
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.redmadrobot.inputmask.MaskedTextChangedListener

class MSDGenderSelector @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attributeSet, defStyleAttr) {

    private var genderSelectedListener: (Gender) -> Unit = {}
    private val binding: ViewMsdGenderSelectorBinding = ViewMsdGenderSelectorBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        val attrs = context.theme.obtainStyledAttributes(attributeSet, R.styleable.MSDGenderSelector, 0, 0)
        attrs.getString(R.styleable.MSDGenderSelector_translationLabelKey)?.let { translationLabelKey ->
            //TODO Add handling translation logic here
            binding.labelTextView.text = translationLabelKey
        }

        binding.maleTextView.setOnDebouncedClickListener {
            onMaleSelected()
            genderSelectedListener(Gender.MALE)
        }

        binding.femaleTextView.setOnDebouncedClickListener {
            onFemaleSelected()
            genderSelectedListener(Gender.FEMALE)
        }

    }

    fun setLabel(label: String){
        binding.labelTextView.text = label
    }

    fun setGenderSelectedListener(genderSelectedListener: (Gender) -> Unit){
        this.genderSelectedListener = genderSelectedListener
    }

    fun setSelectedGender(gender: Gender){
        if (gender == Gender.MALE){
            onMaleSelected()
        } else {
            onFemaleSelected()
        }
    }

    private fun onMaleSelected(){
        binding.maleTextView.isSelected = true
        binding.femaleTextView.isSelected = false
    }

    private fun onFemaleSelected(){
        binding.maleTextView.isSelected = false
        binding.femaleTextView.isSelected= true
    }

    enum class Gender {MALE, FEMALE}

}
