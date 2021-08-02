package com.custom.rgs_android_dom.views.edit_text

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.core.widget.addTextChangedListener
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.ViewMsdSearchInputBinding
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener

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
            binding.searchEditText.hint = translationHintKey
        }

        binding.searchEditText.addTextChangedListener {
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

    fun setFocus(isFocused: Boolean){
        if (isFocused){
            binding.searchEditText.requestFocus()
        } else {
            binding.searchEditText.clearFocus()
        }
    }

}
