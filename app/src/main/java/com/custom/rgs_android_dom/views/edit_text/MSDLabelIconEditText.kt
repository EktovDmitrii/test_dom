package com.custom.rgs_android_dom.views.edit_text

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.InputFilter
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.RelativeLayout
import androidx.core.widget.addTextChangedListener
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.ViewMsdLabelIconEditTextBinding
import com.custom.rgs_android_dom.domain.TranslationInteractor
import com.custom.rgs_android_dom.utils.*
import com.redmadrobot.inputmask.MaskedTextChangedListener

class MSDLabelIconEditText @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attributeSet, defStyleAttr) {

    private val binding: ViewMsdLabelIconEditTextBinding = ViewMsdLabelIconEditTextBinding.inflate(LayoutInflater.from(context), this, true)

    private var textWatcher: (String) -> Unit = {}
    private var onIconClickListener: () -> Unit = {}
    private var onTextChangedListener: (String, Boolean) -> Unit = { _, _ -> }

    private var isFromUser = true

    private val maskedValueChangedListener = object : MaskedTextChangedListener.ValueListener {
        override fun onTextChanged(
            maskFilled: Boolean,
            extractedValue: String,
            formattedValue: String
        ) {
            if (isFromUser){
                onTextChangedListener(formattedValue, maskFilled)
            }
        }
    }
    private var maskedTextChangedListener: MaskedTextChangedListener? = null

    init {
        val attrs = context.theme.obtainStyledAttributes(attributeSet, R.styleable.MSDLabelIconEditText, 0, 0)
        attrs.getString(R.styleable.MSDLabelIconEditText_translationHintKey)?.let { translationHintKey ->
            //TODO Add handling translation logic here
            binding.valueEditText.hint = TranslationInteractor.getTranslation(translationHintKey)
        }

        attrs.getString(R.styleable.MSDLabelIconEditText_translationLabelKey)?.let { translationLabelKey ->
            //TODO Add handling translation logic here
            binding.labelTextView.text = TranslationInteractor.getTranslation(translationLabelKey)
        }

        attrs.getString(R.styleable.MSDLabelIconEditText_mask)?.let {mask->
            setMask(mask)
        }

        attrs.getDrawable(R.styleable.MSDLabelIconEditText_icon)?.let {icon->
            setIcon(icon)
        }

        val imeOptions = attrs.getInt(R.styleable.MSDLabelIconEditText_android_imeOptions, EditorInfo.IME_ACTION_NEXT)
        binding.valueEditText.imeOptions = imeOptions

        val inputType = attrs.getInt(R.styleable.MSDLabelIconEditText_android_inputType, InputType.TYPE_CLASS_TEXT)
        binding.valueEditText.inputType = inputType

        val lines = attrs.getInt(R.styleable.MSDLabelIconEditText_android_lines, 1)
        binding.valueEditText.maxLines = lines

        val textAllCaps = attrs.getBoolean(R.styleable.MSDLabelIconEditText_android_textAllCaps, false)
        binding.valueEditText.isAllCaps = textAllCaps

        val isFocused = attrs.getBoolean(R.styleable.MSDLabelIconEditText_isFocused, false)
        if (isFocused) {
            binding.valueEditText.requestFocus()
        }

        attrs.getInt(R.styleable.MSDLabelIconEditText_android_maxLength, -1).let { maxLength->
            if (maxLength != -1){
                binding.valueEditText.filters += InputFilter.LengthFilter(maxLength)
            }
        }

        attrs.getString(R.styleable.MSDLabelIconEditText_android_digits)?.let {digits->
            val digitsRegex = Regex("[^${digits}]")
            val filter = InputFilter { inputText, _, _, _, _, _ ->
                var str: String = inputText.toString()
                str = str.replace(digitsRegex, "")

                if (str.length == inputText.length) null else str
            }

            binding.valueEditText.filters += filter
        }

        binding.valueEditText.addTextChangedListener {
            if (isFromUser){
                textWatcher(it.toString())
            }

        }

        binding.iconImageView.setOnDebouncedClickListener {
            onIconClickListener()
        }
    }

    fun setText(text: String){
        isFromUser = false
        binding.valueEditText.text = text.toEditable()
        isFromUser = true
    }

    fun setTextFromUser(text: String){
        binding.valueEditText.setText(text)
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

    fun setIcon(image: Int){
        binding.iconImageView.setImageResource(image)
    }

    fun setIcon(image: Drawable){
        binding.iconImageView.setImageDrawable(image)
    }

    fun setIcon(image: String){
        GlideApp.with(context)
            .load(image)
            .into(binding.iconImageView)
    }

    fun setOnIconClickListener(onIconClickListener: () -> Unit){
        this.onIconClickListener = onIconClickListener
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
                binding.iconImageView.isEnabled = true
                super.setEnabled(true)
            }
            State.DISABLED -> {
                binding.containerRelativeLayout.setBackgroundResource(R.drawable.rectangle_filled_secondary_900_alpha14_stroke_seconday_250_1dp_radius_8dp)
                binding.valueEditText.setTextColor(context.getColor(R.color.secondary400))
                binding.secondaryTextView.gone()
                binding.valueEditText.isEnabled = false
                binding.iconImageView.isEnabled = false
                super.setEnabled(false)
            }
            State.ERROR -> {
                binding.containerRelativeLayout.setBackgroundResource(R.drawable.rectangle_stroke_1dp_error_500_radius_8dp)
                binding.valueEditText.setTextColor(context.getColor(R.color.error500))
                binding.secondaryTextView.text = secondaryText
                binding.secondaryTextView.setTextColor(context.getColor(R.color.error500))
                binding.secondaryTextView.visibleIf(secondaryText.isNotEmpty())
                binding.valueEditText.isEnabled = true
                binding.iconImageView.isEnabled = true
                super.setEnabled(true)
            }
            State.SUCCESS -> {
                binding.containerRelativeLayout.setBackgroundResource(R.drawable.rectangle_stroke_1dp_success_500_radius_8dp)
                binding.valueEditText.setTextColor(context.getColor(R.color.success500))
                binding.secondaryTextView.text = secondaryText
                binding.secondaryTextView.setTextColor(context.getColor(R.color.success500))
                binding.secondaryTextView.visibleIf(secondaryText.isNotEmpty())
                binding.valueEditText.isEnabled = true
                binding.iconImageView.isEnabled = true
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
