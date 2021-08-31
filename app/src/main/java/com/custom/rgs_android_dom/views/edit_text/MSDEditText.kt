package com.custom.rgs_android_dom.views.edit_text

import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.RelativeLayout
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.ViewMsdEditTextBinding
import com.custom.rgs_android_dom.utils.TranslationHelper

class MSDEditText @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attributeSet, defStyleAttr) {

    private val binding: ViewMsdEditTextBinding = ViewMsdEditTextBinding.inflate(LayoutInflater.from(context), this, true)
    private var textWatcher: (String) -> Unit = {}

    init {
        val attrs = context.theme.obtainStyledAttributes(attributeSet, R.styleable.MSDEditText, 0, 0)
        attrs.getString(R.styleable.MSDEditText_translationHintKey)?.let { translationHintKey ->
            //TODO Add handling translation logic here
            binding.valueEditText.hint = TranslationHelper.getTranslation(translationHintKey)
        }

        attrs.getString(R.styleable.MSDEditText_prefix)?.let { prefix->
            binding.prefixTextView.text = prefix
        }

        val imeOptions = attrs.getInt(R.styleable.MSDEditText_android_imeOptions, EditorInfo.IME_ACTION_NEXT)
        binding.valueEditText.imeOptions = imeOptions

        val isPrefixVisible = attrs.getBoolean(R.styleable.MSDEditText_prefixVisible, false)
        binding.prefixTextView.isVisible = isPrefixVisible

        val inputType = attrs.getInt(R.styleable.MSDEditText_android_inputType, InputType.TYPE_CLASS_TEXT)
        binding.valueEditText.inputType = inputType

        val lines = attrs.getInt(R.styleable.MSDEditText_android_lines, 1)
        binding.valueEditText.maxLines = lines

        val textAllCaps = attrs.getBoolean(R.styleable.MSDEditText_android_textAllCaps, false)
        binding.valueEditText.isAllCaps = textAllCaps

        val isFocused = attrs.getBoolean(R.styleable.MSDEditText_isFocused, false)
        if (isFocused) {
            binding.valueEditText.requestFocus()
        }

        attrs.getInt(R.styleable.MSDEditText_android_maxLength, -1).let { maxLength->
            if (maxLength != -1){
                binding.valueEditText.filters += InputFilter.LengthFilter(maxLength)
            }
        }

        attrs.getString(R.styleable.MSDEditText_android_digits)?.let {digits->
            val digitsRegex = Regex("[^${digits}]")
            val filter = InputFilter { inputText, _, _, _, _, _ ->
                var str: String = inputText.toString()
                str = str.replace(digitsRegex, "")

                if (str.length == inputText.length) null else str
            }

            binding.valueEditText.filters += filter
        }

        binding.valueEditText.addTextChangedListener {
            textWatcher(it.toString())
        }
    }

    fun setPrefixVisibility(isVisible: Boolean){
        binding.prefixTextView.isVisible = isVisible
    }

    fun setPrefix(prefix: String){
        binding.prefixTextView.text = prefix
    }

    fun setText(text: String){
        binding.valueEditText.setText(text)
    }

    fun setHint(hint: String){
        binding.valueEditText.hint = hint
    }

    fun getText(): String {
        return binding.valueEditText.text.toString()
    }

    fun getPrefix(): String {
        return binding.prefixTextView.text.toString()
    }

    fun addTextWatcher(textWatcher: (String) -> Unit) {
        this.textWatcher = textWatcher
    }

    fun removeTextWatcher(){
        textWatcher = {}
    }

    fun setState(state: State) {
        when(state){
            State.NORMAL -> {
                binding.containerRelativeLayout.setBackgroundResource(R.drawable.rectangle_stroke_1dp_secondary_250_radius_8dp)
                binding.prefixTextView.setTextColor(context.getColor(R.color.secondary900))
                binding.valueEditText.setTextColor(context.getColor(R.color.secondary900))
                super.setEnabled(true)
            }
            State.DISABLED -> {
                binding.containerRelativeLayout.setBackgroundResource(R.drawable.rectangle_filled_secondary_900_alpha14_stroke_seconday_250_1dp_radius_8dp)
                binding.prefixTextView.setTextColor(context.getColor(R.color.secondary400))
                binding.valueEditText.setTextColor(context.getColor(R.color.secondary400))
                super.setEnabled(false)
            }
            State.ERROR -> {
                binding.containerRelativeLayout.setBackgroundResource(R.drawable.rectangle_stroke_1dp_error_500_radius_8dp)
                binding.prefixTextView.setTextColor(context.getColor(R.color.error500))
                binding.valueEditText.setTextColor(context.getColor(R.color.error500))
                super.setEnabled(true)
            }
            State.SUCCESS -> {
                binding.containerRelativeLayout.setBackgroundResource(R.drawable.rectangle_stroke_1dp_success_500_radius_8dp)
                binding.prefixTextView.setTextColor(context.getColor(R.color.success500))
                binding.valueEditText.setTextColor(context.getColor(R.color.success500))
                super.setEnabled(true)
            }
        }
    }

    override fun setEnabled(isEnabled: Boolean){
        val state = if (isEnabled) State.NORMAL else State.DISABLED
        setState(state)
    }

    enum class State {NORMAL, DISABLED, ERROR, SUCCESS}

}
