package com.custom.rgs_android_dom.views.edit_text

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.InputFilter
import android.text.InputType
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.RelativeLayout
import androidx.core.widget.addTextChangedListener
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.ViewMsdMaskedLabelEditTextBinding
import com.redmadrobot.inputmask.MaskedTextChangedListener

class MSDMaskedLabelEditText @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attributeSet, defStyleAttr) {

    private val binding: ViewMsdMaskedLabelEditTextBinding = ViewMsdMaskedLabelEditTextBinding.inflate(LayoutInflater.from(context), this, true)

    private var textWatcher: (String) -> Unit = {}
    private var onTextChangedListener: (String, Boolean) -> Unit = { _, _ -> }

    private var isFromUser = true

    private val maskedValueChangedListener = object : MaskedTextChangedListener.ValueListener {
        override fun onTextChanged(
            maskFilled: Boolean,
            extractedValue: String,
            formattedValue: String
        ) {
            if (isFromUser){
                onTextChangedListener(binding.valueEditText.text.toString(), maskFilled)
            }
        }
    }
    private var maskedTextChangedListener: MaskedTextChangedListener? = null

    init {
        val attrs = context.theme.obtainStyledAttributes(attributeSet, R.styleable.MSDMaskedLabelEditText, 0, 0)
        attrs.getString(R.styleable.MSDMaskedLabelEditText_translationHintKey)?.let { translationHintKey ->
            //TODO Add handling translation logic here
            binding.valueEditText.hint = translationHintKey
        }

        attrs.getString(R.styleable.MSDMaskedLabelEditText_translationLabelKey)?.let { translationLabelKey ->
            //TODO Add handling translation logic here
            binding.labelTextView.text = translationLabelKey
        }

        attrs.getString(R.styleable.MSDMaskedLabelEditText_mask)?.let {mask->
            setMask(mask)
        }

        val imeOptions = attrs.getInt(R.styleable.MSDMaskedLabelEditText_android_imeOptions, EditorInfo.IME_ACTION_NEXT)
        binding.valueEditText.imeOptions = imeOptions

        val inputType = attrs.getInt(R.styleable.MSDMaskedLabelEditText_android_inputType, InputType.TYPE_CLASS_TEXT)
        binding.valueEditText.inputType = inputType

        val lines = attrs.getInt(R.styleable.MSDMaskedLabelEditText_android_lines, 1)
        binding.valueEditText.maxLines = lines

        val textAllCaps = attrs.getBoolean(R.styleable.MSDMaskedLabelEditText_android_textAllCaps, false)
        binding.valueEditText.isAllCaps = textAllCaps

        val isFocused = attrs.getBoolean(R.styleable.MSDMaskedLabelEditText_isFocused, false)
        if (isFocused) {
            binding.valueEditText.requestFocus()
        }

        attrs.getInt(R.styleable.MSDMaskedLabelEditText_android_maxLength, -1).let { maxLength->
            if (maxLength != -1){
                binding.valueEditText.filters += InputFilter.LengthFilter(maxLength)
            }
        }

        //TODO Find out why masked listener manually place +7 at the start of ET
        binding.valueEditText.setText("")

        binding.valueEditText.addTextChangedListener {
            textWatcher(it.toString())
        }



    }

    fun setText(text: String){
        isFromUser = false
        binding.valueEditText.setText(text)
        isFromUser = true
    }

    fun setHint(hint: String){
        binding.valueEditText.hint = hint
    }

    fun getText(): String {
        return binding.valueEditText.text.toString()
    }

    fun addTextWatcher(textWatcher: (String) -> Unit) {
        this.textWatcher = textWatcher
    }

    fun removeTextWatcher(){
        textWatcher = {}
    }

    fun addOnTextChangedListener(onTextChangedListener: (String, Boolean) -> Unit){
        this.onTextChangedListener = onTextChangedListener
    }

    fun removeOnTextChangedListener(){
        onTextChangedListener = { _, _ -> }
    }

    fun setMask(mask: String){
        binding.valueEditText.removeTextChangedListener(maskedTextChangedListener)
        maskedTextChangedListener = MaskedTextChangedListener(mask, binding.valueEditText, maskedValueChangedListener)

        binding.valueEditText.addTextChangedListener(maskedTextChangedListener)
    }

}
