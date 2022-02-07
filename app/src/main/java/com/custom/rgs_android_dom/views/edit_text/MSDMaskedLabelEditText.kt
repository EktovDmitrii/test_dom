package com.custom.rgs_android_dom.views.edit_text

import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.ViewMsdMaskedLabelEditTextBinding
import com.custom.rgs_android_dom.domain.translations.TranslationInteractor
import com.custom.rgs_android_dom.utils.gone
import com.custom.rgs_android_dom.utils.toEditable
import com.custom.rgs_android_dom.utils.visibleIf
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

    private var hint = ""
    private var newHintBuilder = StringBuilder()
    private var hintAlwaysVisible = false

    private val maskedValueChangedListener = object : MaskedTextChangedListener.ValueListener {
        override fun onTextChanged(
            maskFilled: Boolean,
            extractedValue: String,
            formattedValue: String
        ) {
            if (hintAlwaysVisible){
                newHintBuilder.clear()
                for (i in hint.indices){
                    var newChar = if (formattedValue.isNotEmpty() && i < formattedValue.length) {
                        formattedValue[i]
                    } else {
                        hint[i]
                    }
                    newHintBuilder.append(newChar)
                }
                binding.hintEditText.hint = newHintBuilder.toString()
            }

            if (isFromUser){
                onTextChangedListener(formattedValue, maskFilled)
            }
        }
    }
    private var maskedTextChangedListener: MaskedTextChangedListener? = null

    init {
        val attrs = context.theme.obtainStyledAttributes(attributeSet, R.styleable.MSDMaskedLabelEditText, 0, 0)

        hintAlwaysVisible = attrs.getBoolean(R.styleable.MSDMaskedLabelEditText_hintAlwaysVisible, false)
        binding.hintRelativelayout.visibleIf(hintAlwaysVisible)

        attrs.getString(R.styleable.MSDMaskedLabelEditText_translationHintKey)?.let { translationHintKey ->
            //TODO Add handling translation logic here
            setHint(TranslationInteractor.getTranslation(translationHintKey))
        }

        attrs.getString(R.styleable.MSDMaskedLabelEditText_translationLabelKey)?.let { translationLabelKey ->
            //TODO Add handling translation logic here
            binding.labelTextView.text = TranslationInteractor.getTranslation(translationLabelKey)
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

        val titleTextColor = attrs.getColor(R.styleable.MSDMaskedLabelEditText_titleTextColor, ContextCompat.getColor(context, R.color.secondary600))
        binding.labelTextView.setTextColor(titleTextColor)

        val isFocused = attrs.getBoolean(R.styleable.MSDMaskedLabelEditText_isFocused, false)
        if (isFocused) {
            binding.valueEditText.requestFocus()
        }

        attrs.getInt(R.styleable.MSDMaskedLabelEditText_android_maxLength, -1).let { maxLength->
            if (maxLength != -1){
                binding.valueEditText.filters += InputFilter.LengthFilter(maxLength)
            }
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
        if (hintAlwaysVisible){
            binding.hintEditText.hint = hint
        } else {
            binding.valueEditText.hint = hint
        }
        this.hint = hint

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

    fun setState(state: State, secondaryText: String = "") {
        when(state){
            State.NORMAL -> {
                binding.containerRelativeLayout.setBackgroundResource(R.drawable.rectangle_stroke_1dp_secondary_250_radius_8dp)
                binding.valueEditText.setTextColor(context.getColor(R.color.secondary900))
                binding.secondaryTextView.gone()
                binding.valueEditText.isEnabled = true
                super.setEnabled(true)
            }
            State.DISABLED -> {
                binding.containerRelativeLayout.setBackgroundResource(R.drawable.rectangle_filled_secondary_900_alpha14_stroke_seconday_250_1dp_radius_8dp)
                binding.valueEditText.setTextColor(context.getColor(R.color.secondary400))
                binding.valueEditText.isEnabled = false
                binding.secondaryTextView.gone()
                super.setEnabled(false)
            }
            State.ERROR -> {
                binding.containerRelativeLayout.setBackgroundResource(R.drawable.rectangle_stroke_1dp_error_500_radius_8dp)
                binding.valueEditText.setTextColor(context.getColor(R.color.error500))
                binding.secondaryTextView.text = secondaryText
                binding.secondaryTextView.setTextColor(context.getColor(R.color.error500))
                binding.secondaryTextView.visibleIf(secondaryText.isNotEmpty())
                binding.valueEditText.isEnabled = true
                super.setEnabled(true)
            }
            State.SUCCESS -> {
                binding.containerRelativeLayout.setBackgroundResource(R.drawable.rectangle_stroke_1dp_success_500_radius_8dp)
                binding.valueEditText.setTextColor(context.getColor(R.color.success500))
                binding.secondaryTextView.text = secondaryText
                binding.secondaryTextView.setTextColor(context.getColor(R.color.success500))
                binding.secondaryTextView.visibleIf(secondaryText.isNotEmpty())
                binding.valueEditText.isEnabled = true
                super.setEnabled(true)
            }
        }
    }

    override fun setEnabled(isEnabled: Boolean){
        val state = if (isEnabled) State.NORMAL else State.DISABLED
        setState(state)
    }

    enum class State { NORMAL, DISABLED, ERROR, SUCCESS }

}
