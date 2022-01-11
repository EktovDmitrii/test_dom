package com.custom.rgs_android_dom.views.edit_text

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.core.widget.addTextChangedListener
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.ViewMsdSearchInputBinding
import com.custom.rgs_android_dom.domain.translations.TranslationInteractor
import com.custom.rgs_android_dom.utils.*


class MSDSearchInput @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attributeSet, defStyleAttr) {

    private val binding: ViewMsdSearchInputBinding = ViewMsdSearchInputBinding.inflate(LayoutInflater.from(context), this, true)
    private var textWatcher: (String) -> Unit = {}
    private var focusChangedListener: (Boolean) -> Unit = {}
    private var onClearClickListener: () -> Unit = {}

    init {
        val attrs = context.theme.obtainStyledAttributes(attributeSet, R.styleable.MSDSearchInput, 0, 0)
        attrs.getString(R.styleable.MSDSearchInput_translationHintKey)?.let { translationHintKey ->
            //TODO Add handling translation logic here
            binding.searchEditText.hint = TranslationInteractor.getTranslation(translationHintKey)
        }

        attrs.getDimension(R.styleable.MSDSearchInput_inputHeight, -1f).let {
            if (it != -1f){
                val params = binding.rootConstraintLayout.layoutParams
                params.height = it.toInt()
                binding.rootConstraintLayout.layoutParams = params
            }
        }

        val drawableId = attrs.getResourceId(R.styleable.MSDSearchInput_icon, -1)
        if (drawableId != -1) {
            val drawable = VectorDrawableCompat.create(resources, drawableId, null)
            binding.searchIconImageView.setImageDrawable(drawable)
        }


        val isEnabled = attrs.getBoolean(R.styleable.MSDSearchInput_android_enabled, true)
        setEnabled(isEnabled)

        binding.searchEditText.addTextChangedListener {
            binding.clearImageView.visibleIf(it.toString().isNotEmpty())
            textWatcher(it.toString())
        }

        binding.searchEditText.setOnFocusChangeListener { view, isFocused ->
            focusChangedListener(isFocused)
        }

        binding.clearImageView.setOnDebouncedClickListener {
            binding.searchEditText.text?.clear()
            onClearClickListener()
        }

    }

    fun setHint(hint: String) {
        binding.searchEditText.hint = hint
    }

    fun setText(text: String){
        binding.searchEditText.text = text.toEditable()
    }

    fun addTextChangedListener(textWatcher: (String) -> Unit){
        this.textWatcher = textWatcher
    }

    fun removeTextChangedListener(){
        textWatcher = {}
    }

    fun setOnFocusChangedListener(focusChangedListener: (Boolean) -> Unit){
        this.focusChangedListener = focusChangedListener
    }

    fun setOnClearClickListener(onClearClickListener: () -> Unit){
        this.onClearClickListener = onClearClickListener
    }

    fun setOnClickListener(onClickListener: () -> Unit){
        binding.clickerLinearLayout.setOnDebouncedClickListener {
            onClickListener()
        }
    }

    fun setFocus(isFocused: Boolean){
        if (isFocused){
            binding.searchEditText.requestFocus()
        } else {
            binding.searchEditText.clearFocus()
        }
    }

    override fun setEnabled(enabled: Boolean) {
        if (enabled){
            binding.clickerLinearLayout.gone()
        } else {
            binding.clickerLinearLayout.visible()
        }
    }

    fun focus(){
        binding.searchEditText.focus()
        binding.searchEditText.showKeyboard()
    }

    fun unfocus(){
        binding.searchEditText.unFocus()
        binding.searchEditText.hideKeyboard()
        binding.searchEditText.text?.clear()
    }

    fun clear(){
        binding.searchEditText.hideKeyboard()
        binding.searchEditText.text?.clear()
    }

    override fun isFocused(): Boolean {
        return binding.searchEditText.isFocused
    }

}
