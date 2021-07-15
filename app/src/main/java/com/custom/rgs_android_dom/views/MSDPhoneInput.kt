package com.custom.rgs_android_dom.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.LinearLayoutCompat
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kotlinx.android.synthetic.main.view_msd_phone_input.view.*

class MSDPhoneInput @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

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

    fun setCountryCode(countryCode: String){
        countryCodeTextView.text = countryCode
    }

    fun setMask(phoneMask: String, onPhoneChangedListener:(String, Boolean) -> Unit){
        phoneEditText.removeTextChangedListener(maskedTextChangedListener)

        this.onPhoneChangedListener = onPhoneChangedListener
        maskedTextChangedListener = MaskedTextChangedListener(phoneMask, phoneEditText, maskedValueChangedListener)

        phoneEditText.addTextChangedListener(maskedTextChangedListener)

    }

    fun setPhone(phone: String) {
        isFromUser = false
        phoneEditText.setText(phone)
        phoneEditText.setSelection(phone.length)
        isFromUser = true
    }
}
