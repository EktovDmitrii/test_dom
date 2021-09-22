package com.custom.rgs_android_dom.views.edit_text

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import androidx.core.widget.addTextChangedListener
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.ViewMsdTextInputLayoutBinding
import com.custom.rgs_android_dom.utils.DecimalDigitsInputFilter
import com.custom.rgs_android_dom.utils.TranslationHelper
import com.custom.rgs_android_dom.utils.toEditable

class MSDTextInputLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attributeSet, defStyleAttr) {

    private val binding: ViewMsdTextInputLayoutBinding = ViewMsdTextInputLayoutBinding.inflate(LayoutInflater.from(context), this, true)
    private var textWatcher: (String) -> Unit = {}

    private var isFromUser = true

    init {
        val attrs = context.theme.obtainStyledAttributes(attributeSet, R.styleable.MSDTextInputLayout, 0, 0)
        attrs.getString(R.styleable.MSDTextInputLayout_translationHintKey)?.let { translationHintKey ->
            //TODO Add handling translation logic here
            binding.containerTextInputLayout.hint = TranslationHelper.getTranslation(translationHintKey)
        }

        val imeOptions = attrs.getInt(R.styleable.MSDTextInputLayout_android_imeOptions, EditorInfo.IME_ACTION_NEXT)
        binding.valueEditText.imeOptions = imeOptions

        val inputType = attrs.getInt(R.styleable.MSDTextInputLayout_android_inputType, InputType.TYPE_CLASS_TEXT)
        binding.valueEditText.inputType = inputType

        val lines = attrs.getInt(R.styleable.MSDTextInputLayout_android_lines, -1)
        if (lines != -1){
            binding.valueEditText.maxLines = lines
        }


        val textAllCaps = attrs.getBoolean(R.styleable.MSDTextInputLayout_android_textAllCaps, false)
        binding.valueEditText.isAllCaps = textAllCaps

        val isFocused = attrs.getBoolean(R.styleable.MSDTextInputLayout_isFocused, false)
        if (isFocused) {
            binding.valueEditText.requestFocus()
        }

        attrs.getInt(R.styleable.MSDTextInputLayout_android_maxLength, -1).let { maxLength->
            if (maxLength != -1){
                binding.valueEditText.filters += LengthFilter(maxLength)
            }
        }

        attrs.getString(R.styleable.MSDTextInputLayout_android_digits)?.let {digits->
            val digitsRegex = Regex("[^${digits}]")
            val filter = InputFilter { inputText, _, _, _, _, _ ->
                var str: String = inputText.toString()
                str = str.replace(digitsRegex, "")

                if (str.length == inputText.length) null else str
            }

            binding.valueEditText.filters += filter
        }

        attrs.getString(R.styleable.MSDTextInputLayout_decimalLimit)?.let {
            val digitsArr = it.split(".")
            setDigitsLimit(digitsArr[0].toInt(), digitsArr[1].toInt())
        }

        binding.valueEditText.addTextChangedListener {
            if (isFromUser){
                textWatcher(it.toString())
            }

        }
    }

    fun setText(text: String){
        isFromUser = false
        binding.valueEditText.text = text.toEditable()
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

    fun setState(state: State, secondaryText: String = "") {
        when (state){
            State.NORMAL -> {
                binding.valueEditText.setTextColor(context.getColor(R.color.secondary900))
                binding.containerTextInputLayout.boxStrokeColor = context.getColor(R.color.primary500)
            }
            State.ERROR -> {
                binding.valueEditText.setTextColor(context.getColor(R.color.error500))
                binding.containerTextInputLayout.boxStrokeColor = context.getColor(R.color.error500)
            }
        }
    }

    private fun setDigitsLimit(digitsBeforeZero: Int, digitsAfterZero: Int) {
        binding.valueEditText.filters = arrayOf(DecimalDigitsInputFilter(digitsBeforeZero, digitsAfterZero))
    }

    enum class State { NORMAL, ERROR}

}
