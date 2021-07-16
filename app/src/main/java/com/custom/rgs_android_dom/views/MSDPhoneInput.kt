package com.custom.rgs_android_dom.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.LinearLayoutCompat
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.ViewMsdPhoneInputBinding
import com.custom.rgs_android_dom.utils.GlideApp
import com.custom.rgs_android_dom.utils.gone
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.visible
import com.redmadrobot.inputmask.MaskedTextChangedListener

class MSDPhoneInput @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attributeSet, defStyleAttr) {

    companion object {
        private const val DEFAULT_DIGIT = 9
    }

    private val binding: ViewMsdPhoneInputBinding = ViewMsdPhoneInputBinding.inflate(LayoutInflater.from(context), this, true)

    private var onCountryClickListener: (String) -> Unit = {}
    private var onPhoneChangedListener: (String, Boolean) -> Unit = { _, _ -> }
    private var isFromUser = true
    private var countryCode = ""

    private var maskedTextChangedListener: MaskedTextChangedListener? = null

    private val maskedValueChangedListener = object : MaskedTextChangedListener.ValueListener {
        override fun onTextChanged(
            maskFilled: Boolean,
            extractedValue: String,
            formattedValue: String
        ) {
            if (isFromUser){
                onPhoneChangedListener("$countryCode$formattedValue", maskFilled)

                if (maskFilled){
                    removeErrorState()
                } else {
                    setErrorState()
                }
            }
        }
    }

    init {

        val attrs = context.theme.obtainStyledAttributes(attributeSet, R.styleable.MSDPhoneInput, 0, 0)
        attrs.getString(R.styleable.MSDPhoneInput_phoneMask)?.let { phoneMask->
            setMask(phoneMask)
        }
        attrs.getDrawable(R.styleable.MSDPhoneInput_countryImage)?.let { countryImage->
            setCountryImage(countryImage)
        }

        binding.countryLinearLayout.setOnDebouncedClickListener {
            onCountryClickListener(countryCode)
        }
    }

    fun setOnCountryClickListener(onCountryClickListener: (String) -> Unit){
        this.onCountryClickListener = onCountryClickListener
    }

    fun setCountryImage(image: Int){
        binding.countryImageView.setImageResource(image)
    }

    fun setCountryImage(image: Drawable){
        binding.countryImageView.setImageDrawable(image)
    }

    fun setCountryImage(image: String){
        GlideApp.with(context)
            .load(image)
            .into(binding.countryImageView)
    }

    fun setMask(phoneMask: String, onPhoneChangedListener:(String, Boolean) -> Unit){
        countryCode = extractCountryCode(phoneMask)
        val mask = phoneMask.replace(countryCode, "")
        binding.countryCodeTextView.text = countryCode

        binding.phoneEditText.removeTextChangedListener(maskedTextChangedListener)
        this.onPhoneChangedListener = onPhoneChangedListener
        maskedTextChangedListener = MaskedTextChangedListener(mask, binding.phoneEditText, maskedValueChangedListener)

        binding.phoneEditText.addTextChangedListener(maskedTextChangedListener)

        val hint = makeHint(mask)
        binding.phoneEditText.hint = hint
    }

    fun setOnPhoneChangedListener(onPhoneChangedListener:(String, Boolean) -> Unit){
        this.onPhoneChangedListener = onPhoneChangedListener
    }

    fun setMask(phoneMask: String){
        countryCode = extractCountryCode(phoneMask)
        val mask = phoneMask.replace(countryCode, "")
        binding.countryCodeTextView.text = countryCode

        binding.phoneEditText.removeTextChangedListener(maskedTextChangedListener)
        maskedTextChangedListener = MaskedTextChangedListener(mask, binding.phoneEditText, maskedValueChangedListener)

        binding.phoneEditText.addTextChangedListener(maskedTextChangedListener)

        val hint = makeHint(mask)
        binding.phoneEditText.hint = hint
    }

    fun setPhone(phone: String) {
        isFromUser = false
        binding.phoneEditText.setText(phone)
        binding.phoneEditText.setSelection(phone.length)
        isFromUser = true
    }

    private fun extractCountryCode(phoneMask: String): String{
        return phoneMask.substring(0, phoneMask.indexOf("["))
    }

    private fun makeHint(mask: String): String {
        return mask.replace("\\d".toRegex(), DEFAULT_DIGIT.toString())
            .replace("[\\[\\]]".toRegex(), "")
    }

    private fun setErrorState(){
        binding.phoneContainerConstraintLayout.setBackgroundResource(R.drawable.rectangle_stroke_1dp_dangerous_red_radius_8dp)
        binding.countryCodeTextView.setTextColor(context.getColor(R.color.dangerousRed))
        binding.phoneEditText.setTextColor(context.getColor(R.color.dangerousRed))
        binding.errorTextView.visible()
    }

    private fun removeErrorState(){
        binding.phoneContainerConstraintLayout.setBackgroundResource(R.drawable.rectangle_stroke_1dp_secondary_250_radius_8dp)
        binding.countryCodeTextView.setTextColor(context.getColor(R.color.secondary900))
        binding.phoneEditText.setTextColor(context.getColor(R.color.secondary900))
        binding.errorTextView.gone()
    }
}
