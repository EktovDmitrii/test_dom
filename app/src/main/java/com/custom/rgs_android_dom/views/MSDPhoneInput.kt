package com.custom.rgs_android_dom.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.LinearLayoutCompat
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.utils.GlideApp
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kotlinx.android.synthetic.main.view_msd_phone_input.view.*

class MSDPhoneInput @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attributeSet, defStyleAttr) {

    companion object {
        private const val DEFAULT_DIGIT = 9
    }

    private var onCodeClickListener: () -> Unit = {}
    private var onPhoneChangedListener: (String, Boolean) -> Unit = { _, _ -> }
    private var isFromUser = true

    private var maskedTextChangedListener: MaskedTextChangedListener? = null

    private val maskedValueChangedListener = object : MaskedTextChangedListener.ValueListener {
        override fun onTextChanged(
            maskFilled: Boolean,
            extractedValue: String,
            formattedValue: String
        ) {
            if (isFromUser){
                onPhoneChangedListener(formattedValue, maskFilled)
            }
        }
    }

    init {
        inflate(context, R.layout.view_msd_phone_input, this)

        val attrs = context.theme.obtainStyledAttributes(attributeSet, R.styleable.MSDPhoneInput, 0, 0)
        attrs.getString(R.styleable.MSDPhoneInput_phoneMask)?.let { phoneMask->
            setMask(phoneMask)
        }
        attrs.getString(R.styleable.MSDPhoneInput_countryCode)?.let {countryCode->
            setCountryCode(countryCode)
        }
        attrs.getDrawable(R.styleable.MSDPhoneInput_countryImage)?.let { countryImage->
            setCountryImage(countryImage)
        }

        countryPickerLinearLayout.setOnDebouncedClickListener {
            onCodeClickListener()
        }
    }

    fun setOnCodeClickListener(onCodeClickListener: () -> Unit){
        this.onCodeClickListener = onCodeClickListener
    }

    fun setCountryImage(image: Int){
        countryImageView.setImageResource(image)
    }

    fun setCountryImage(image: Drawable){
        countryImageView.setImageDrawable(image)
    }

    fun setCountryImage(image: String){
        GlideApp.with(context)
            .load(image)
            .into(countryImageView)
    }

    fun setCountryCode(countryCode: String){
        countryCodeTextView.text = countryCode
    }

    fun setMask(phoneMask: String, onPhoneChangedListener:(String, Boolean) -> Unit){
        phoneEditText.removeTextChangedListener(maskedTextChangedListener)

        this.onPhoneChangedListener = onPhoneChangedListener
        maskedTextChangedListener = MaskedTextChangedListener(phoneMask, phoneEditText, maskedValueChangedListener)

        phoneEditText.addTextChangedListener(maskedTextChangedListener)

        val hint = phoneMask.replace("[0-9]+/*\\.*[0-9]*", DEFAULT_DIGIT.toString())
            .replace("[", "")
            .replace("]","")
        phoneEditText.hint = hint
    }

    fun setOnPhoneChangedListener(onPhoneChangedListener:(String, Boolean) -> Unit){
        this.onPhoneChangedListener = onPhoneChangedListener
    }

    fun setMask(phoneMask: String){
        phoneEditText.removeTextChangedListener(maskedTextChangedListener)

        maskedTextChangedListener = MaskedTextChangedListener(phoneMask, phoneEditText, maskedValueChangedListener)

        phoneEditText.addTextChangedListener(maskedTextChangedListener)

        val hint = phoneMask.replace("[0-9]".toRegex(), DEFAULT_DIGIT.toString())
            .replace("[", "")
            .replace("]","")
        phoneEditText.hint = hint
    }

    fun setPhone(phone: String) {
        isFromUser = false
        phoneEditText.setText(phone)
        phoneEditText.setSelection(phone.length)
        isFromUser = true
    }
}
