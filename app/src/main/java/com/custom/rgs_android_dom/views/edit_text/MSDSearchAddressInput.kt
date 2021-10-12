package com.custom.rgs_android_dom.views.edit_text

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.core.widget.addTextChangedListener
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.ViewMsdSearchAddressInputBinding
import com.custom.rgs_android_dom.domain.TranslationInteractor
import com.custom.rgs_android_dom.utils.*

class MSDSearchAddressInput @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attributeSet, defStyleAttr) {

    private val binding: ViewMsdSearchAddressInputBinding = ViewMsdSearchAddressInputBinding.inflate(LayoutInflater.from(context), this, true)
    private var textWatcher: (String) -> Unit = {}
    private var focusChangedListener: (Boolean) -> Unit = {}
    private var onClearClickListener: () -> Unit = {}

    init {
        val attrs = context.theme.obtainStyledAttributes(attributeSet, R.styleable.MSDSearchAddressInput, 0, 0)
        attrs.getString(R.styleable.MSDSearchAddressInput_translationHintKey)?.let { translationHintKey ->
            //TODO Add handling translation logic here
            binding.searchEditText.hint = TranslationInteractor.getTranslation(translationHintKey)
        }

        attrs.getDimension(R.styleable.MSDSearchAddressInput_inputHeight, -1f).let {
            if (it != -1f){
                val params = binding.rootConstraintLayout.layoutParams
                params.height = it.toInt()
                binding.rootConstraintLayout.layoutParams = params
            }
        }

        val drawableId = attrs.getResourceId(R.styleable.MSDSearchAddressInput_icon, -1)
        if (drawableId != -1) {
            val drawable = VectorDrawableCompat.create(resources, drawableId, null)
            binding.searchIconImageView.setImageDrawable(drawable)
        }


        val isEnabled = attrs.getBoolean(R.styleable.MSDSearchAddressInput_android_enabled, true)
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

    override fun isFocused(): Boolean {
        return binding.searchEditText.isFocused
    }

}
