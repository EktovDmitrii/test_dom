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
import com.custom.rgs_android_dom.databinding.ViewMsdIconEditTextBinding
import com.custom.rgs_android_dom.utils.GlideApp
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.redmadrobot.inputmask.MaskedTextChangedListener

class MSDIconEditText @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attributeSet, defStyleAttr) {

    private val binding: ViewMsdIconEditTextBinding = ViewMsdIconEditTextBinding.inflate(LayoutInflater.from(context), this, true)

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
                onTextChangedListener(binding.valueEditText.text.toString(), maskFilled)
            }
        }
    }
    private var maskedTextChangedListener: MaskedTextChangedListener? = null

    init {
        val attrs = context.theme.obtainStyledAttributes(attributeSet, R.styleable.MSDIconEditText, 0, 0)
        attrs.getString(R.styleable.MSDIconEditText_translationHintKey)?.let { translationHintKey ->
            //TODO Add handling translation logic here
            binding.valueEditText.hint = translationHintKey
        }

        attrs.getString(R.styleable.MSDIconEditText_mask)?.let {mask->
            setMask(mask)
        }

        attrs.getDrawable(R.styleable.MSDIconEditText_icon)?.let {icon->
            setIcon(icon)
        }

        val imeOptions = attrs.getInt(R.styleable.MSDIconEditText_android_imeOptions, EditorInfo.IME_ACTION_NEXT)
        binding.valueEditText.imeOptions = imeOptions

        val inputType = attrs.getInt(R.styleable.MSDIconEditText_android_inputType, InputType.TYPE_CLASS_TEXT)
        binding.valueEditText.inputType = inputType

        val lines = attrs.getInt(R.styleable.MSDIconEditText_android_lines, 1)
        binding.valueEditText.maxLines = lines

        val textAllCaps = attrs.getBoolean(R.styleable.MSDIconEditText_android_textAllCaps, false)
        binding.valueEditText.isAllCaps = textAllCaps

        val isFocused = attrs.getBoolean(R.styleable.MSDIconEditText_isFocused, false)
        if (isFocused) {
            binding.valueEditText.requestFocus()
        }

        binding.valueEditText.addTextChangedListener {
            textWatcher(it.toString())
        }

        binding.iconImageView.setOnDebouncedClickListener {
            onIconClickListener()
        }

        attrs.getInt(R.styleable.MSDIconEditText_android_maxLength, -1).let { maxLength->
            if (maxLength != -1){
                binding.valueEditText.filters += InputFilter.LengthFilter(maxLength)
            }
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

}
