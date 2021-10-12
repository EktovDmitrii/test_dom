package com.custom.rgs_android_dom.views.text

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.ViewMsdTextViewLoaderBinding
import com.custom.rgs_android_dom.domain.translations.TranslationInteractor
import com.custom.rgs_android_dom.utils.gone
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.visible

class MSDTextViewLoader @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attributeSet, defStyleAttr) {


    private val binding: ViewMsdTextViewLoaderBinding = ViewMsdTextViewLoaderBinding.inflate(LayoutInflater.from(context), this, true)
    private var isTextViewEnabled = binding.actionTextView.isEnabled
    private var originalText = binding.actionTextView.text.toString()
    private var textWidth = binding.actionTextView.width

    init {
        val attrs = context.theme.obtainStyledAttributes(attributeSet, R.styleable.MSDTextViewLoader, 0, 0)

        attrs.getString(R.styleable.MSDTextViewLoader_translationTextKey)?.let { translationTextKey ->
            //TODO Add handling translation logic here
            setText(TranslationInteractor.getTranslation(translationTextKey))
        }

        val enabled = attrs.getBoolean(R.styleable.MSDTextViewLoader_android_enabled, true)
        isEnabled = enabled

        binding.actionTextView.background = ContextCompat.getDrawable(context, R.drawable.button_filled_primary_background)
    }

    fun setLoading(isLoading: Boolean){
        if (isLoading){
            binding.loadingProgressBar.visible()
            binding.actionTextView.isEnabled = false
            binding.actionTextView.text = ""
            binding.actionTextView.width = textWidth
        } else {
            binding.loadingProgressBar.gone()
            binding.actionTextView.isEnabled = isTextViewEnabled
            setText(originalText)
        }
    }

    override fun setEnabled(enabled: Boolean) {
        isTextViewEnabled = enabled
        binding.actionTextView.isEnabled = enabled
        super.setEnabled(enabled)
    }

    fun setOnDebouncedClickListener(action: () -> Unit) {
        binding.actionTextView.setOnDebouncedClickListener {
            action.invoke()
        }
    }

    fun setText(text: String){
        originalText = text
        binding.actionTextView.text = text
        binding.actionTextView.measure(0, 0)
        textWidth = binding.actionTextView.measuredWidth
    }

}
