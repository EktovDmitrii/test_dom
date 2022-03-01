package com.custom.rgs_android_dom.views.edit_text

import android.content.Context
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import androidx.core.widget.addTextChangedListener
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.ViewMsdTextInputLayoutBinding
import com.custom.rgs_android_dom.domain.translations.TranslationInteractor
import com.custom.rgs_android_dom.utils.DecimalDigitsInputFilter
import com.custom.rgs_android_dom.utils.hideKeyboard
import com.custom.rgs_android_dom.utils.toEditable

class MSDTextInputLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attributeSet, defStyleAttr) {

    private val binding: ViewMsdTextInputLayoutBinding = ViewMsdTextInputLayoutBinding.inflate(LayoutInflater.from(context), this, true)
    private var textWatcher: (String) -> Unit = {}

    private var isFromUser = true
    private var hasDecimalFilter = false
    private var unfocusOnDone = false

    private var hint: String = ""
    private var focusedHint: String = ""

    init {
        val attrs = context.theme.obtainStyledAttributes(attributeSet, R.styleable.MSDTextInputLayout, 0, 0)
        attrs.getString(R.styleable.MSDTextInputLayout_translationHintKey)?.let { translationHintKey ->
            //TODO Add handling translation logic here
            binding.containerTextInputLayout.hint = TranslationInteractor.getTranslation(translationHintKey)
            hint = TranslationInteractor.getTranslation(translationHintKey)
        }

        attrs.getString(R.styleable.MSDTextInputLayout_focusedTranslationHintKey)?.let { focusedTranslationHintKey->
            focusedHint =  TranslationInteractor.getTranslation(focusedTranslationHintKey)
        }

        attrs.getString(R.styleable.MSDTextInputLayout_android_text)?.let{
            setText(it)
        }

        attrs.getString(R.styleable.MSDTextInputLayout_placeholderText)?.let{
            binding.containerTextInputLayout.placeholderText = it
        }

        unfocusOnDone = attrs.getBoolean(R.styleable.MSDTextInputLayout_unfocusOnDone, false)

        binding.containerTextInputLayout.isEnabled = attrs.getBoolean(R.styleable.MSDTextInputLayout_enabled, true)

        val imeOptions = attrs.getInt(R.styleable.MSDTextInputLayout_android_imeOptions, EditorInfo.IME_ACTION_NEXT)
        binding.valueEditText.imeOptions = imeOptions

        val inputType = attrs.getInt(R.styleable.MSDTextInputLayout_android_inputType, InputType.TYPE_CLASS_TEXT)
        binding.valueEditText.inputType = inputType

        val lines = attrs.getInt(R.styleable.MSDTextInputLayout_android_lines, -1)
        if (lines != -1){
            binding.valueEditText.maxLines = lines
            if (lines > 1){
                binding.valueEditText.setOnTouchListener { view, event ->
                    view.parent.requestDisallowInterceptTouchEvent(true)
                    if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                        view.parent.requestDisallowInterceptTouchEvent(false)
                    }
                    return@setOnTouchListener false
                }
            }
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
            hasDecimalFilter = true
        }

        binding.valueEditText.addTextChangedListener {
            if (isFromUser){
                if (hasDecimalFilter && it.toString() == "."){
                    binding.valueEditText.setText("")
                } else {
                    textWatcher(it.toString())
                }
            }

        }

        if (focusedHint.isNotEmpty()){
            binding.valueEditText.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus){
                    setHint(focusedHint)
                } else {
                    setHint(hint)
                }
            }
        }

        if (unfocusOnDone){
            binding.valueEditText.setOnEditorActionListener { _, keyCode, _ ->
                if (keyCode == EditorInfo.IME_ACTION_DONE){
                    binding.valueEditText.clearFocus()
                }
                binding.valueEditText.hideKeyboard()
                binding.containerTextInputLayout.clearFocus()
                binding.containerTextInputLayout.hideKeyboard()
                true
            }
        }

        attrs.getDimension(R.styleable.MSDTextInputLayout_boxStrokeWidth,1f).let { boxStrokeWidth ->
            binding.containerTextInputLayout.boxStrokeWidth = boxStrokeWidth.toInt()
        }

        attrs.getDimension(R.styleable.MSDTextInputLayout_boxStrokeWidthFocused,1f).let { boxStrokeWidthFocused ->
            binding.containerTextInputLayout.boxStrokeWidthFocused = boxStrokeWidthFocused.toInt()
        }
    }

    fun setText(text: String){
        isFromUser = false
        binding.valueEditText.text = text.toEditable()
        isFromUser = true
    }

    fun setHint(hint: String){
        binding.containerTextInputLayout.hint = hint
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

    fun toggleEnable(isEnabled: Boolean) {
        binding.valueEditText.clearFocus()
        binding.valueEditText.hideKeyboard()
        binding.containerTextInputLayout.clearFocus()
        binding.containerTextInputLayout.hideKeyboard()

        binding.containerTextInputLayout.isEnabled = isEnabled
    }

    fun setSelection(length: Int){
        binding.valueEditText.setSelection(length)
    }

    private fun setDigitsLimit(digitsBeforeZero: Int, digitsAfterZero: Int) {
        binding.valueEditText.filters = arrayOf(DecimalDigitsInputFilter(digitsBeforeZero, digitsAfterZero))
    }

    enum class State { NORMAL, ERROR}

}
